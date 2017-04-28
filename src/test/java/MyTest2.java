
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.aaa.ssh.service.UserService;



public class MyTest2  {
	
	@Test
	public void testApplyMajorDao(){
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring/applicationContext*.xml");
//		ApplyMajorDao majorDao = applicationContext.getBean(ApplyMajorDao.class);
//		List<ApplyMajor> list = majorDao.getAll();
//		for (ApplyMajor applyMajor : list) {
//			System.out.println(applyMajor.getMajor());
//		}
		
//		PoliticJuryVoteBiz bean = applicationContext.getBean(PoliticJuryVoteBiz.class);
//		EnginJuryVo a = bean.getEnginJuryVoteVoByProjectId(1);
//		System.out.println(a);
	
		UserService userDao = applicationContext.getBean(UserService.class);
		userDao.test();
		
	}

}
