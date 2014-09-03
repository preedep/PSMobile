package com.epro.psmobile.data;

public interface TransactionStmtHolder {
	String deleteStatement() throws Exception;
	String insertStatement() throws Exception;
	String updateStatement() throws Exception;
}
