package com.afd.dao;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.afd.pojo.Cluster;
import com.afd.pojo.Document;

@Repository(value = "clusterDao")
@EnableTransactionManagement()
public class ClusterDao extends Dao {

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public void insertCluster(double high, double low, int docCount){
		Cluster cluster = new Cluster();
		cluster.setHigh(high);
		cluster.setLow(low);
		cluster.setDocCount(docCount);
		template.save(cluster);
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public List<Cluster> getClusterList() {
		return (List<Cluster>) template.find("select c from Cluster c order by c.high desc");
	}
}
