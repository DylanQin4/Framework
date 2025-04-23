package itu.framework.webservice.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import itu.framework.webservice.entity.PassengerType;
import itu.framework.webservice.repository.PassengerTypeRepository;

import java.util.List;

@Service
public class PassengerTypeService {
	private final PassengerTypeRepository repo;

	public PassengerTypeService(PassengerTypeRepository repo) {
		this.repo = repo;
	}

	public List<PassengerType> listAll() {
		return repo.findAll().stream()
		.sorted((a,b) -> {
			Integer sa = a.getStartAge() == null ? -1 : a.getStartAge();
			Integer sb = b.getStartAge() == null ? -1 : b.getStartAge();
			return sa.compareTo(sb);
		}).toList();
	}

	public PassengerType getById(Integer id) {
		return repo.findById(id).orElseThrow(() -> new IllegalArgumentException("PassengerType not found: " + id));
	}

	@Transactional
	public PassengerType create(PassengerType pt) {
		validateRange(pt);
		return repo.save(pt);
	}

	@Transactional
	public PassengerType update(Integer id, PassengerType form) {
		validateRange(form);
		PassengerType existing = getById(id);
		existing.setTypeName(form.getTypeName());
		existing.setStartAge(form.getStartAge());
		existing.setEndAge(form.getEndAge());
		return repo.save(existing);
	}

	@Transactional
	public void delete(Integer id) {
		repo.deleteById(id);
	}

	private void validateRange(PassengerType pt) {
		Integer s = pt.getStartAge();
		Integer e = pt.getEndAge();
		if (s != null && e != null && s > e) {
			throw new IllegalArgumentException("Start age must be <= end age");
		}
	}
}

