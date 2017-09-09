package com.afd.dao;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Repository(value = "impWordDao")
@EnableTransactionManagement()
public class ImpWordDao extends Dao {

}
