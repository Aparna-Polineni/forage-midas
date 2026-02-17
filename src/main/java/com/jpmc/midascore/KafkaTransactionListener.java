package com.jpmc.midascore;

import com.jpmc.midascore.entity.TransactionRecord;
import com.jpmc.midascore.entity.UserRecord;
import com.jpmc.midascore.foundation.Transaction;
import com.jpmc.midascore.repository.TransactionRecordRepository;
import com.jpmc.midascore.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaTransactionListener {

    private final UserRepository userRepository;
    private final TransactionRecordRepository transactionRecordRepository;

    @Value("${general.kafka-topic}")
    private String topic;

    @KafkaListener(topics = "${general.kafka-topic}", groupId ="midas-group")
    public void listen(Transaction transaction){
        UserRecord sender = userRepository.findById(transaction.getSenderId());
        UserRecord recipient = userRepository.findById(transaction.getRecipientId());

        //validate users exist
        if(sender == null || recipient == null){
            return;
        }

        Float amount = transaction.getAmount();

        //validate balance
        if(sender.getBalance() < amount){
            return;
        }

        //update balances
        sender.setBalance(sender.getBalance()-amount);
        recipient.setBalance(recipient.getBalance()+amount);

        userRepository.save(sender);
        userRepository.save(recipient);

        //save transaction record
        TransactionRecord record = new TransactionRecord(sender, recipient, amount);

        transactionRecordRepository.save(record);

    }

    public KafkaTransactionListener(UserRepository userRepository, TransactionRecordRepository transactionRecordRepository){
        this.userRepository = userRepository;
        this.transactionRecordRepository = transactionRecordRepository;
    }
}
