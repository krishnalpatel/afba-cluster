package com.afd.business;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.afd.dao.ClusterDao;
import com.afd.dao.DocumentDao;
import com.afd.pojo.Cluster;
import com.afd.pojo.Document;

@Component(value = "clusterizer")
@Scope("prototype")
public class Clusterizer {

	@Autowired
	private DocumentDao documentDao;

	@Autowired
	private ClusterDao clusterDao;
	
	@Autowired
	private DatumboxCaller datumboxCaller;
	
	@Autowired
	private PropertyReader propertyReader;

	/*
	 * static final String avg = "AVG"; static final String max = "MAX"; static
	 * final String min = "MIN";
	 */
	public void createClusters() {
		/*
		 * double simAvg = documentDao.getSimilarity(avg); double simMax =
		 * documentDao.getSimilarity(max); double simMin =
		 * documentDao.getSimilarity(min);
		 */

		// initClusters();

		List<Document> docList = documentDao.getDocumentListOrderBySimilarity();
		List<Cluster> clusterList = clusterDao.getClusterList();

		int i = 0;
		Cluster cluster = clusterList.get(i);
		for (Document document : docList) {
			while (document.getSimilarity() < cluster.getLow()) {
				cluster = clusterList.get(++i);
			}
			if (document.getSimilarity() < cluster.getHigh() && document.getSimilarity() >= cluster.getLow()) {
				document.setClusterId(cluster.getId());
				documentDao.insertDocument(document);
			}
		}

	}

	public void initClusters() {

		double high = 5.0;
		double low = 3.5;
		double diff = 0.5;

		while (low >= 0.0) {
			clusterDao.insertCluster(high, low, 0);
			high -= diff;
			low -= diff;
		}

	}

	public void compareWithinClusters() {
		//TODO: write the matrices in a file instead of prinitng
		List<Cluster> clusterList = clusterDao.getClusterList();

		for (Cluster cluster : clusterList) {
			List<Document> docList = documentDao.getDocListByClusterId(cluster.getId());
			int size = docList.size();
			
			double[][] matrix = new double[size][size];
			
			for(int i =0; i<size; i++){
				matrix[i][i] = 1;
				matrix[i][i] = 1;
			}
			
			if (size > 1) {
				//List<Document> docList2 = docList1;
				for (int i=0; i<size; i++){
					for(int j=i+1; j<size; j++){
						//match docs docList.get(i) & docList.get(j)
						Map<String, Double> map = datumboxCaller.compareDocs(docList.get(i).getDocName(), docList.get(j).getDocName());
						matrix[i][j] = map.get("Oliver");
						matrix[j][i] = map.get("Shingle");
					}
				}
			}
			
			//persist both metrics in a file 
			persist(matrix, cluster, docList);
			
			//printing the matrix
			System.out.println("\nDocument Similarity Matrix for Cluster "+cluster.getId());
			System.out.println("\nOliver Metric");
			System.out.print("id");
			for(int i=0; i<size; i++)
				System.out.print("\t"+docList.get(i).getId());
			
			for(int i=0; i<size; i++){
				System.out.println(docList.get(i).getId());
				for(int j=0; j<size; j++){
					System.out.print("\t");
					if(j>=i)
						System.out.print(matrix[i][j]);
				}
			}
			System.out.println("\nShingle Metric");
			System.out.print("id");
			for(int i=0; i<size; i++)
				System.out.print("\t"+docList.get(i).getId());
			
			for(int i=0; i<size; i++){
				System.out.print("\n"+docList.get(i).getId());
				for(int j=0; j<size; j++){
					System.out.print("\t");
					if(j>=i)
						System.out.print(matrix[j][i]);
				}
			}
			//print ends
			
		}

	}

	private void persist(double[][] matrix, Cluster cluster, List<Document> docList) {
		List<String> lines = new ArrayList<>();
		String line = "id";
		for (Document document : docList) {
			line = line.concat(",").concat(document.getId().toString());
		}
		lines.add(line);
		
		for(int i=0; i<docList.size(); i++){
			line = docList.get(i).getId().toString();
			for(int j = 0; j<docList.size(); j++){
				line = line.concat(",").concat(String.valueOf(matrix[i][j]));
			}
			lines.add(line);
		}
		
		Path file = Paths.get(propertyReader.readProperty("file.write.location")+"Cluster"+cluster.getId().toString()+".csv");
		try {
			Files.write(file, lines, Charset.forName("UTF-8"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	public DocumentDao getDocumentDao() {
		return documentDao;
	}

	public void setDocumentDao(DocumentDao documentDao) {
		this.documentDao = documentDao;
	}

	public ClusterDao getClusterDao() {
		return clusterDao;
	}

	public void setClusterDao(ClusterDao clusterDao) {
		this.clusterDao = clusterDao;
	}

	public DatumboxCaller getDatumboxCaller() {
		return datumboxCaller;
	}

	public void setDatumboxCaller(DatumboxCaller datumboxCaller) {
		this.datumboxCaller = datumboxCaller;
	}

	public PropertyReader getPropertyReader() {
		return propertyReader;
	}

	public void setPropertyReader(PropertyReader propertyReader) {
		this.propertyReader = propertyReader;
	}

}
