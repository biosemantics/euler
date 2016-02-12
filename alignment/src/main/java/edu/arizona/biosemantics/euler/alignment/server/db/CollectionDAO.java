package edu.arizona.biosemantics.euler.alignment.server.db;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.arizona.biosemantics.common.log.LogLevel;
import edu.arizona.biosemantics.euler.alignment.server.Configuration;
import edu.arizona.biosemantics.euler.alignment.server.db.Query.QueryException;
import edu.arizona.biosemantics.euler.alignment.shared.model.Collection;
import edu.arizona.biosemantics.euler.alignment.shared.model.Model;

public class CollectionDAO {
		
	public CollectionDAO() {} 
	
	public boolean isValidSecret(int id, String secret) throws QueryException  {
		try(Query query = new Query("SELECT secret FROM alignment_collection WHERE id = ?")) {
			query.setParameter(1, id);
			ResultSet result = query.execute();
			while(result.next()) {
				String validSecret = result.getString(1);
				return validSecret.equals(secret);
			}
		} catch(QueryException | SQLException e) {
			log(LogLevel.ERROR, "Query Exception", e);
			throw new QueryException(e);
		}
		return false;
	}
	
	public Collection get(int id) throws Exception  {
		Collection collection = null;
		try(Query query = new Query("SELECT * FROM alignment_collection WHERE id = ?")) {
			query.setParameter(1, id);
			ResultSet result = query.execute();
			while(result.next()) {
				collection = createCollection(result);
			}
		} catch(QueryException | SQLException e) {
			log(LogLevel.ERROR, "Query Exception", e);
			throw new QueryException(e);
		}
		
		try(Query query = new Query("UPDATE alignment_collection SET lastretrieved = ? WHERE id = ?")) {
			query.setParameter(2, id);
			Date date = new Date();
			query.setParameter(1, new Timestamp(date.getTime()));
			query.execute();
		} catch(QueryException e) {
			log(LogLevel.ERROR, "Query Exception", e);
			throw e;
		}
		
		return collection;
	}
	
	private Collection createCollection(ResultSet result) throws Exception {
		int id = result.getInt("id");
		String secret = result.getString("secret");
		return new Collection(id, secret, unserializeModel(id));
	}
	
	private void serializeModel(Collection collection) throws IOException {
		try(ObjectOutput output = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(
				Configuration.collectionsPath + File.separator + collection.getId() + File.separator + "Model.ser")))) {
			output.writeObject(collection.getModel());
		}
	}
	
	private Model unserializeModel(int collectionId) throws FileNotFoundException, IOException, ClassNotFoundException {
		try(ObjectInput input = new ObjectInputStream(new BufferedInputStream(new FileInputStream(
				Configuration.collectionsPath + File.separator + collectionId + File.separator + "Model.ser")))) {
			Model model = (Model)input.readObject();
			return model;
		}
	}
	
	public Collection insert(Collection collection) throws QueryException, IOException  {
		if(collection.hasId()) 
			this.remove(collection);
		try(Query insert = new Query("INSERT INTO `alignment_collection` (`secret`) VALUES(?)")) {
			insert.setParameter(1, collection.getSecret());
			insert.execute();
			ResultSet generatedKeys = insert.getGeneratedKeys();
			generatedKeys.next();
			int id = generatedKeys.getInt(1);
			collection.setId(id);
			
			File file = new File(Configuration.collectionsPath + File.separator + id);
			file.mkdirs();
			serializeModel(collection);
		} catch(QueryException | SQLException e) {
			log(LogLevel.ERROR, "Query Exception", e);
			throw new QueryException(e);
		}
		return collection;
	}
	
	public void update(Collection collection) throws QueryException, IOException  {		
		try(Query query = new Query("UPDATE alignment_collection SET secret = ? WHERE id = ?")) {
			query.setParameter(1, collection.getSecret());
			query.setParameter(2, collection.getId());
			query.execute();
		} catch(QueryException e) {
			log(LogLevel.ERROR, "Query Exception", e);
			throw e;
		}
	}
	
	public void remove(Collection collection) throws QueryException  {
		try(Query query = new Query("DELETE FROM alignment_collection WHERE id = ?")) {
			query.setParameter(1, collection.getId());
			query.execute();
		} catch(QueryException e) {
			log(LogLevel.ERROR, "Query Exception", e);
			throw e;
		}
	}
}