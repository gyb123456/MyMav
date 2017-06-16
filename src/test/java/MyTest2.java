
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.aaa.ssh.dao.UserDao;



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
	
//		UserService service = applicationContext.getBean(UserService.class);
//		service.saveManyRel();
		
//		UserController userController = applicationContext.getBean(UserController.class);
		
		UserDao dao = applicationContext.getBean(UserDao.class);
//		dao.findMatchingStationRel();
//		dao.haveStationId("123");
		
		/* for(EnumTest name :EnumTest.values()){
    		 System.out.println(name+" : "+name.getContext());
    	 }
    	 System.out.println(EnumTest.FRANK.getDeclaringClass());*/
    	 
		List list = new ArrayList<Integer>();
		list.add(10);
		System.out.println("1=="+list.hashCode());
		list  = new ArrayList<Integer>();
		System.out.println("2=="+list.hashCode());
	}
	
	 
	public enum EnumTest {
	     FRANK("The given name of me"),
	     LIU("The family name of me");
	     private String context;
	     private String getContext(){
	    	 return this.context;
	     }
	     private EnumTest(String context){
	    	 this.context = context;
	     }
	  
	} 
}
