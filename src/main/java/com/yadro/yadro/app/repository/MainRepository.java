package com.yadro.yadro.app.repository;

import com.yadro.yadro.app.exception.PersonNotFoundException;
import com.yadro.yadro.app.model.Person;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class MainRepository {

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<Person> rowMapper = (rs, rowNum) -> {
        Person p = new Person();
        p.setId(rs.getLong("id"));
        p.setGender(rs.getString("gender"));
        p.setFirstName(rs.getString("first_name"));
        p.setLastName(rs.getString("last_name"));
        p.setPhone(rs.getString("phone"));
        p.setEmail(rs.getString("email"));
        p.setCity(rs.getString("city"));
        p.setAddress(rs.getString("address"));
        return p;
    };

    public void saveAll(List<Person> people) {
        String sql = "INSERT INTO persons (gender, first_name, last_name, phone, email, city, address) VALUES (?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.batchUpdate(sql, people, 100, (ps, person) -> {
            ps.setString(1, person.getGender());
            ps.setString(2, person.getFirstName());
            ps.setString(3, person.getLastName());
            ps.setString(4, person.getPhone());
            ps.setString(5, person.getEmail());
            ps.setString(6, person.getCity());
            ps.setString(7, person.getAddress());
        });
    }

    public Page<Person> findAll(Pageable pageable) {
        String countSql = "SELECT COUNT(*) FROM persons";
        int total = jdbcTemplate.queryForObject(countSql, Integer.class);
        String sql = "SELECT * FROM persons ORDER BY id LIMIT ? OFFSET ?";
        List<Person> content = jdbcTemplate.query(sql, rowMapper, pageable.getPageSize(), pageable.getOffset());
        return new PageImpl<>(content, pageable, total);
    }

    public Person findById(Long id) {
        try {
            String sql = "SELECT * FROM persons WHERE id = ?";
            return jdbcTemplate.queryForObject(sql, rowMapper, id);
        } catch (EmptyResultDataAccessException e) {
            throw new PersonNotFoundException(id);
        }
    }

    public Person findRandom() {
        String sql = "SELECT * FROM persons ORDER BY RAND() LIMIT 1";
        try {
            return jdbcTemplate.queryForObject(sql, rowMapper);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public long count() {
        return jdbcTemplate.queryForObject("SELECT COUNT(*) FROM persons", Long.class);
    }

    public void deleteAll() {
        jdbcTemplate.update("DELETE FROM persons");
        jdbcTemplate.update("ALTER TABLE persons ALTER COLUMN id RESTART WITH 1");
    }
}