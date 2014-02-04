/**
 * 
 */
package org.springframework.samples.travel;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.servlet.view.JspTemplateViewResolver;

/**
 * A JspTemplateViewResolverConfigurationTests.
 *
 * @author jsimao
 */
@ContextConfiguration("classpath:JspTemplateViewResolver-config.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class JspTemplateViewResolverConfigurationTests {
	@Autowired JspTemplateViewResolver viewResolver;
	
	@Test
	public void test() {
		assertEquals("common/standard", viewResolver.getTemplateName("hotels/"));
		assertEquals("common/standard-alt", viewResolver.getTemplateName("hotels/show"));
	}

}
