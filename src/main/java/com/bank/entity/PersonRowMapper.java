package com.bank.entity;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class PersonRowMapper implements RowMapper<Person>{
	@Override
	public Person mapRow(ResultSet rs, int rowNum) throws SQLException {
		Person person = new Person();
		person.setName(rs.getString(3));
		person.setPassword(rs.getString(4));
		person.setGmail(rs.getString(5));
		return person;
	}

}
