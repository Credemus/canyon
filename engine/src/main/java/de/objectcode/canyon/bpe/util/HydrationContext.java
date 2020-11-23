package de.objectcode.canyon.bpe.util;

public class HydrationContext {
	public final static int CLASSIC_SCHEMA = 0;

	public final static int COMPRESSED_SCHEMA = 1;

	private int fSchema = CLASSIC_SCHEMA;


	public HydrationContext() {
		fSchema = CLASSIC_SCHEMA;
		if (System.getProperty("de.objectcode.canyon.bpe.util.HydrationContext.useCompressedSchema")!=null)
			fSchema = COMPRESSED_SCHEMA;
	}

	public HydrationContext(int schema) {
		fSchema = schema;
	}

	public int getSchema() {
		return fSchema;
	}
}
