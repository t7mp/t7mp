package com.googlecode.t7mp;

import java.io.IOException;
import java.util.List;
import java.util.Properties;

import org.apache.maven.plugin.logging.Log;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class DefaultTomcatArtifactDescriptorReaderTest {
	
	private Log mockLog = Mockito.mock(Log.class);
	
	@Before
	public void setUp(){
		Mockito.doAnswer(new Answer<CharSequence>() {

			@Override
			public CharSequence answer(InvocationOnMock invocation) throws Throwable {
				System.out.println(invocation.getArguments()[0]);
				return null;
			}
		}).when(mockLog).debug(Mockito.anyString());
	}
	
	@Test
	public void testResolveTomcat7(){
		DefaultTomcatArtifactDescriptorReader reader = new DefaultTomcatArtifactDescriptorReader(mockLog);
		reader.getMajorVersion("7.1.2");
		List<JarArtifact> artifactList = reader.getJarArtifacts("7.1.2");
		Assert.assertNotNull(artifactList);
		Assert.assertEquals("15 Artifacte sind gefordert", 15, artifactList.size());
		System.out.println(artifactList);
	}
	
	@Test(expected=TomcatSetupException.class)
	public void testResolveTomcatNullVersion(){
		DefaultTomcatArtifactDescriptorReader reader = new DefaultTomcatArtifactDescriptorReader(mockLog);
		reader.getJarArtifacts(null);
	}
	
	@Test(expected=TomcatSetupException.class)
	public void testResolveTomcatEmptyVersion(){
		DefaultTomcatArtifactDescriptorReader reader = new DefaultTomcatArtifactDescriptorReader(mockLog);
		reader.getJarArtifacts("");
	}
	
	@Test
	public void testMajorVersion(){
		DefaultTomcatArtifactDescriptorReader reader = new DefaultTomcatArtifactDescriptorReader(mockLog);
		String majorVersion = reader.getMajorVersion("7.1.2");
		Assert.assertNotNull(majorVersion);
		Assert.assertNotSame("", majorVersion);
		Assert.assertEquals("7", majorVersion);
	}
	
	@Test(expected=TomcatSetupException.class)
	public void testMajorVersionFailure(){
		DefaultTomcatArtifactDescriptorReader reader = new DefaultTomcatArtifactDescriptorReader(mockLog);
		reader.getMajorVersion("71.2");
	}
	
	@Test(expected=TomcatSetupException.class)
	public void testPropertiesLoaderException() throws IOException {
		DefaultTomcatArtifactDescriptorReader reader = new DefaultTomcatArtifactDescriptorReader(mockLog, new ExceptionalPropertiesLoader());
		reader.getJarArtifacts("7.1.2");
	}
	
	public static class ExceptionalPropertiesLoader implements PropertiesLoader {

		@Override
		public Properties load(Class<?> clazz, String propertiesFilename) throws IOException {
			throw new IOException("LOADERTESTEXCEPTION");
		}
		
	}

}
