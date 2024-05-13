package com.losaxa.framework.user.listener;

import com.losaxa.core.common.JsonUtil;
import com.losaxa.framework.user.domian.User;
import com.mongodb.client.model.changestream.ChangeStreamDocument;
import lombok.extern.slf4j.Slf4j;
import org.bson.BsonTimestamp;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.ChangeStreamOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.messaging.*;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Slf4j
@Component
public class MongoElasticListener {

    @Autowired
    private MongoTemplate mongoTemplate;

    @PostConstruct
    public void listen() {
        MessageListenerContainer container = new DefaultMessageListenerContainer(mongoTemplate);
        container.start();
        MessageListener<ChangeStreamDocument<Document>, User> listener =message -> {
            User body = message.getBody();
            ChangeStreamDocument<Document> raw = message.getRaw();
            log.info("\nlisten mongo opt-type: {} ,id: {} ,body: {}", raw.getOperationType().getValue(),raw.getDocumentKey() ,JsonUtil.toJson(body));
        };
        ChangeStreamOptions changeStreamOptions = ChangeStreamOptions.builder().resumeAt(new BsonTimestamp(1640966400000L)).build();
        ChangeStreamRequest.ChangeStreamRequestOptions options = new ChangeStreamRequest.ChangeStreamRequestOptions("user", "user", changeStreamOptions);
        Subscription subscription = container.register(new ChangeStreamRequest<>(listener, options), User.class);

        //container.stop();
    }


}
