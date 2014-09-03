package com.epro.psmobile.sync;

public interface TransferProgressListener {
	void transferred(String messageTypeId,long allOfDataSize,long num);

}
