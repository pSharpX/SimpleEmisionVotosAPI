package edu.cibertec.votoelectronico.repository.impl;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.UserTransaction;

import static com.arjuna.ats.jta.UserTransaction.userTransaction;

import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.TypedQuery;

import org.hibernate.search.jpa.FullTextQuery;
import org.hibernate.search.query.DatabaseRetrievalMethod;
import org.hibernate.search.query.ObjectLookupMethod;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import edu.cibertec.votoelectronico.application.HibernateConfig;
import edu.cibertec.votoelectronico.domain.Voto;
import edu.cibertec.votoelectronico.domain.complex.VotoResumen;
import edu.cibertec.votoelectronico.repository.VotoRepository;
import edu.cibertec.votoelectronico.shared.Pagination;

@Repository
public class VotoRepositoryImpl extends BaseRepository implements VotoRepository {

	private final Logger LOG = LoggerFactory.getLogger(VotoRepositoryImpl.class);

	private static final String Q_GET_ALL = "SELECT c FROM Voto c";
	private static final String Q_GET_BY_DNI = "SELECT c FROM Voto c WHERE dni = :dni";

	private static final String NQ_DELETE_ALL = "QN_VOTO_DELETE_ALL";

	@Override
	public Voto find(String id) {
		return this.em.find(Voto.class, id);
	}

	@Override
	public List<Voto> getAll() {
		TypedQuery<Voto> query = this.em.createQuery(Q_GET_ALL, Voto.class);
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Pagination<Voto> getAll(int page, int size) {
		QueryBuilder queryBuilder = this.fullTextEntityManager.getSearchFactory().buildQueryBuilder()
				.forEntity(Voto.class).get();
		org.apache.lucene.search.Query query = queryBuilder.all().createQuery();
		FullTextQuery fullTextQuery = this.fullTextEntityManager.createFullTextQuery(query, Voto.class);
		fullTextQuery.initializeObjectsWith(ObjectLookupMethod.SKIP, DatabaseRetrievalMethod.FIND_BY_ID);
		fullTextQuery.setFirstResult(((page - 1) * size));
		fullTextQuery.setMaxResults(size);
		List<Voto> results = fullTextQuery.getResultList();
		int totalItems = fullTextQuery.getResultSize();
		int totalPages = (int) Math.ceil((double) totalItems / size);

		Pagination<Voto> pagination = new Pagination<Voto>();
		pagination.setResult(results);
		pagination.setCurrentPage(page);
		pagination.setPageSize(size);
		pagination.setTotalItems(totalItems);
		pagination.setTotalPages(totalPages);
		return pagination;
	}

	@Override
	public void create(Voto object) throws Exception {
		UserTransaction tx = null;
		try {
			tx = userTransaction();
			tx.begin();
			HibernateConfig.getSessionFactory().getCurrentSession().persist(object);
			tx.commit();
		} catch (Exception e) {
			tx.rollback();
			throw e;
		}
	}

	@Override
	public void update(Voto object) {
		this.em.merge(object);
	}

	@Override
	public void delete(Voto object) {
		this.em.remove(object);
	}

	@Override
	public void deleteAll() throws Exception {
		UserTransaction tx = null;
		try {
			tx = userTransaction();
			tx.begin();
			this.em.createNamedQuery(NQ_DELETE_ALL, Voto.class).executeUpdate();
			tx.commit();
		} catch (Exception e) {
			tx.rollback();
			throw e;
		}
	}

	@Override
	public List<VotoResumen> groupByGrupoPolitico() {
		String lookup = "{ '$lookup': { 'from': 'grupoPoliticos', 'localField': 'grupoPolitico', 'foreignField': '_id', 'as': 'grupoPolitico' } }";
		String group = "{ '$group': { '_id': '$grupoPolitico.nombre', 'votantes': { '$push': '$dni' }, 'cantidad': { '$sum': 1 } } }";
		String unwind = "{ '$unwind': '$_id'} ";
		String project = "{ '$project' : { '_id': 0, 'grupoPolitico': '$_id', 'votantes': 1, 'cantidad': 1 } }";
		String nativeQuery = "db.votos.aggregate([" + lookup + "," + group + "," + unwind + "," + project + "])";

		@SuppressWarnings("unchecked")
		List<Object[]> results = this.em.createNativeQuery(nativeQuery).getResultList();

		@SuppressWarnings("unchecked")
		List<VotoResumen> collection = results.stream().map((Object[] tuple) -> new VotoResumen((tuple[2]).toString(),
				(List<String>) tuple[0], ((Number) tuple[1]).intValue())).collect(Collectors.toList());
		return collection;
	}

	@Override
	public List<VotoResumen> groupByGrupoPolitico(String grupoPolitico) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<VotoResumen> groupByGrupoPolitico(String grupoPolitico, Date fecha) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<VotoResumen> groupByGrupoPolitico(Date fecha) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Voto findByDni(String dni) throws Exception {
		Voto voto = null;
		try {
			TypedQuery<Voto> query = this.em.createQuery(Q_GET_BY_DNI, Voto.class).setParameter("dni", dni);
			voto = query.getSingleResult();
			return voto;
		} catch (NoResultException e) {
			LOG.error("No se encontro voto con dni " + dni + ". " + e.getMessage());
			return voto;
		} catch (NonUniqueResultException e) {
			LOG.error("Se encontraron varios votos con el mismo dni " + dni + ". " + e.getMessage());
			throw e;
		}
	}

}
