package de.objectcode.canyon.bpe.util;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

/**
 * @author junglas
 */
public interface IStateHolder
{
  public void dehydrate ( HydrationContext context, ObjectOutput out ) throws IOException;
  
  public void hydrate (  HydrationContext context, ObjectInput in ) throws IOException;
}
