package com.tech.data;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.BulkRequest;
import co.elastic.clients.elasticsearch.core.BulkResponse;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.bulk.BulkOperation;
import co.elastic.clients.elasticsearch.core.bulk.BulkResponseItem;
import co.elastic.clients.elasticsearch.core.search.Hit;
import com.tech.finger.DocField;
import com.tech.finger.ExtractFinger;
import com.tech.finger.SimHash;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Wang
 * 2023/6/6
 */
@Slf4j
@Service
public class ESOperator {

    private final ElasticsearchClient elasticsearchClient;

    public ESOperator(ElasticsearchClient elasticsearchClient) {
        this.elasticsearchClient = elasticsearchClient;
    }


    public void setFinger(String index) {
        Map<BigInteger, Document> simMap = new HashMap<>();
        try {
            List<String> fields = Arrays.asList("title", "content");
            BulkRequest.Builder bulkBuilder = new BulkRequest.Builder();

            SearchResponse<Document> docResponse = elasticsearchClient.search(s -> s
                            .index(index)
                            .size(1000)
                            .source(config -> config
                                    .filter(filter -> filter
                                            .includes(fields)))
                    , Document.class);
            List<Hit<Document>> hits = docResponse.hits().hits();
            for (Hit<Document> hit : hits) {
                Document source = hit.source();
                HashMap<String, String> sourceMap = new HashMap<>();
                assert source != null;
                sourceMap.put("title", source.getTitle());
                sourceMap.put("content", source.getContent());
                BigInteger bigInteger = extractFinger(sourceMap);
                String finger = bigInteger.toString();
                source.setFinger(finger);
                source.setFinalSim(getFinalSim(bigInteger, simMap));
                bulkBuilder.operations(op -> op
                        .update(u -> u
                                .index(index)
                                .id(hit.id())
                                .action(a -> a.doc(source))));
            }
            BulkResponse bulkResponse = elasticsearchClient.bulk(bulkBuilder.build());
            Map<String, List<BulkResponseItem>> collect = bulkResponse.items().stream().collect(Collectors.groupingBy(item -> item.result()));
            collect.forEach((k, v) -> {
                log.info(k + ": ", v.size());
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean compareFinger(BigInteger bigInteger1, BigInteger bigInteger2) {
        return hamingDistance(splitFour(bigInteger1), splitFour(bigInteger2)) < 10;
    }

    /**
     * 计算被分为四个short类型的数 的 海明距离
     *
     * @param value1 1
     * @param value2 2
     * @return int
     */
    private static int hamingDistance(Short[] value1, Short[] value2) {
        int distance = 0;
        for (int i = 0; i < value1.length; i++) {
            short a = value1[i];
            short b = value2[i];
            int c = a ^ b;// 两个值异或的值
            for (int index = 0; index < 16; index++) {
                if (isBitSet(c, index)) {
                    distance++;
                }
            }
        }
        return distance;
    }

    private static boolean isBitSet(int b, int pos) {
        return (b & (1 << pos)) != 0;
    }

    private static Short[] splitFour(final BigInteger data) {
        int length = 4, size = 16;
        BigInteger mask = new BigInteger("1").shiftLeft(size).subtract(new BigInteger("1"));
        BigInteger value = data;
		Short[] result = new Short[length];
		for (int i = 0; i < length; i++) {
			result[length - 1 - i] = value.and(mask).shortValue();
			value = value.shiftRight(size);
		}
		return result;
	}

    public BigInteger extractFinger(Map<String, String> source) {
        List<DocField> fieldList = source.entrySet().stream()
                .map(k -> new DocField(k.getKey(), k.getValue(), 1))
                .collect(Collectors.toList());
        SimHash simHash = new ExtractFinger(fieldList).finger();
        return simHash.getStrSimHash();
    }

    public String getFinalSim(BigInteger finger, Map<BigInteger, Document> simMap) {
        if (simMap.size() > 0) {
            for (BigInteger s : simMap.keySet()) {
                if (compareFinger(s, finger)) {
                    return simMap.get(s).getFinalSim();
                }
            }
        }
        String finalSim = UUID.randomUUID().toString().replaceAll("-", "");
        simMap.put(finger, new Document(finger.toString(), finalSim));
        return finalSim;
    }

    public void bulkIndex(String index, List<Map<String, Object>> docs) {
        BulkRequest of = BulkRequest.of(builder -> builder.operations(docs.stream()
                .map(doc -> BulkOperation.of(b -> b.index(i -> i.index(index).id(doc.get("docNo").toString()).document(doc)))).collect(Collectors.toList()))
        );
        try {
            BulkResponse bulkResponse = elasticsearchClient.bulk(of);
//            bulkResponse.items().stream().collect(Collectors.groupingBy(BulkResponseItem::result)).forEach((k , v) -> {
//                log.info(k + ": " + v.size());
//            });
            if (bulkResponse.errors()) {
                log.error("写入出现异常");
            } else {
                log.info("写入完成");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
