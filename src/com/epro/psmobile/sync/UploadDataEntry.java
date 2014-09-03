package com.epro.psmobile.sync;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;

import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;



public class UploadDataEntry extends MultipartEntity {

	private TransferProgressListener listener;
	private String messageTypeId;
	private long contentLenght = -1;

	public UploadDataEntry(
			TransferProgressListener transferListener) {
		// TODO Auto-generated constructor stub
		//this.contentLenght = dataSize;
		this.listener = transferListener;
	}

	public UploadDataEntry(HttpMultipartMode mode,
			
			TransferProgressListener transferListener) {
		super(mode);
		// TODO Auto-generated constructor stub
		//this.contentLenght = dataSize;
		this.listener = transferListener;
	}

	public UploadDataEntry(HttpMultipartMode mode, 
			String boundary,
			Charset charset,
		
			TransferProgressListener transferListener) {
		super(mode, boundary, charset);
		// TODO Auto-generated constructor stub
		//this.contentLenght = dataSize;
		this.listener = transferListener;
	}
	
	/* (non-Javadoc)
	 * @see org.apache.http.entity.mime.MultipartEntity#writeTo(java.io.OutputStream)
	 */
	@Override
	public void writeTo(OutputStream outstream) throws IOException {
		// TODO Auto-generated method stub

		super.writeTo(new CountingOutputStream(outstream,
				this.messageTypeId,
				contentLenght, 
				this.listener));
	}
	public void setContentLength(long contentLenght)
	{
		this.contentLenght = contentLenght;
	}
	
	public static class CountingOutputStream extends FilterOutputStream
	{
 
		private final TransferProgressListener listener;
		private long transferred;
		private String messageTypeId;
		private long contentLenght;
		private long percentage = 100;
		
		public CountingOutputStream(final OutputStream out, 
				String messageTypeId,
				long contentLenght,
				final TransferProgressListener listener)
		{
			super(out);
			this.listener = listener;
			this.transferred = 0;

			
			
			this.messageTypeId = messageTypeId;
			this.contentLenght = contentLenght;
		}
 
		public void write(byte[] b, int off, int len) throws IOException
		{
			out.write(b, off, len);
			this.transferred += len;
			if (this.listener != null)
			{
				long transferrredInPercenTage = 
						(this.transferred * percentage /this.contentLenght);
				this.listener.transferred(this.messageTypeId,percentage,transferrredInPercenTage);
			}
		}
 
		public void write(int b) throws IOException
		{
			out.write(b);
			this.transferred++;
			if (this.listener != null){
				long transferrredInPercenTage = 
						(this.transferred * percentage /this.contentLenght);

				this.listener.transferred(this.messageTypeId,percentage,transferrredInPercenTage);
			}
		}
	}
}
