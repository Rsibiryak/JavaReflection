package com.site.basetest;

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import com.site.datadriven.DataDriven;
import com.site.datadriven.YamlTestData;

/**
 * Common action to all tests.
 * 
 * @author Alexander_Suvorov
 *
 */
public class AbstractTest {
	private boolean isDataDriven;
	private final String resourcesDirectory = "src/test/resources/";
	private final String testsDirectory = "src/test/java/";

	public AbstractTest() {
		Class<?> testClass = getClass();
		isDataDriven = isAnnotationPresent(testClass, DataDriven.class);
		if (isDataDriven) {
			copyFile(getYamlName(testClass));
			matchYamlDataToClassFieds();
		}
	}

	/**
	 * Check if the class has an annotation.
	 * 
	 * @param clazz
	 * @param annotationClass
	 * @return
	 */
	private static boolean isAnnotationPresent(Class<?> clazz, Class<? extends Annotation> annotationClass) {
		boolean isHaveAnnotation = clazz.isAnnotationPresent(annotationClass);
		return isHaveAnnotation;
	}

	/**
	 * Match test data from yaml file with test fields.
	 */
	private void matchYamlDataToClassFieds() {
		YamlTestData testData = readYamlFile();
		try {
			Class<?> testClass = Class.forName(getClass().getName());
			Field[] fields = testClass.getDeclaredFields();

			Class<YamlTestData> testDataClass = YamlTestData.class;
			Method[] methods = testDataClass.getMethods();

			for (Field field : fields) {
				field.setAccessible(true);
				String fieldName = field.getName();

				for (Method method : methods) {
					String methodName = method.getName();

					if (methodName.toLowerCase().contains(fieldName.toLowerCase())
					        && methodName.toLowerCase().contains("get")) {
						field.set(this, method.invoke(testData));
						break;
					}
				}
			}
		}
		catch (ClassNotFoundException | IllegalAccessException | InvocationTargetException ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * Read test data from yaml file to YamlTestData class.
	 * 
	 * @return
	 */
	private YamlTestData readYamlFile() {
		try {
			Yaml yaml = new Yaml(new Constructor(YamlTestData.class));
			InputStream inputStream = ClassLoader.getSystemResourceAsStream("ReadYaml.yaml");
			return yaml.load(inputStream);
		}
		catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	/**
	 * Copy file from test folder to resource folder.
	 */
	private void copyFile(String fileName) {
		try {
			// String yamlFileName = fileName;
			// //getClass().getSimpleName().replace("Test", ".yaml");

			Path sourceDirectory = Paths.get(getRelativePath(getClass()) + fileName);
			Path targetDirectory = Paths.get(resourcesDirectory + fileName);

			Files.copy(sourceDirectory, targetDirectory, StandardCopyOption.REPLACE_EXISTING);
		}
		catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	private String getYamlName(Class<?> clazz) {
		return clazz.getSimpleName().replace("Test", ".yaml");
	}

	/**
	 * Get relative path for yaml file
	 * 
	 * @param clazz
	 * @param yamlFileName
	 * @return
	 */
	private String getRelativePath(Class<?> clazz) {
		String relativePath = testsDirectory + clazz.getName();
		relativePath = relativePath.replace(".", "/").substring(0,
		        relativePath.length() - relativePath.lastIndexOf("/") + 1);

		return relativePath;
	}
}
