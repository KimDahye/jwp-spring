### 2. 서버가 시작하는 시점에 부모 ApplicationContext와 자식 ApplicationContext가 초기화되는 과정에 대해 구체적으로 설명해라.
- 톰캣이 시작되면, 가장 먼저 web.xml을 읽는다.
- web.xml에 <listener>로 org.springframework.web.context.ContextLoaderListener 가 등록되어 있으므로, ContextLoaderListener의 contextInitialized 메소드를 실행한다.
- 이 메소드 안에서 initWebApplicationContext(event.getServletContext()); 를 실행한다. 이 때, web.xml의 contextParam으로 applicationContext.xml의 path가 저장되어 있다. 따라서 applicationContext를 읽어 부모 스프링 컨테이너를 만든다.
- loadOnStartUp 설정이 되어 있지 않으므로, 처음 요청이 올 때, DispatcherServlet이 초기화된다.
- 초기화 되는 과정은 다음과 같다. DispatcherServlet의 부모 클래스인 HttpServletBean의 init()메소드가 먼저 실행되게 된다. 이 init() 메소드 안에서 initServletBean() 메소드를 호출한다. initServletBean 메소드는 FrameworkServlet 클래스에서 구현되어 있다. 구현부분을 살펴보면, initServletBean() 안에서 createWebApplicationContext(rootContext)를 실행한다. (이 이상은 더이상 자세히 설명하지 않겠다.) 이 안에서 next-servlet.xml을 읽어 앞서 만든 부모 스프링 컨테이너를 부모로 갖는 자식 스프링 컨테이너가 만들어지게 된다. 



### 3. 서버 시작 후 http://localhost:8080으로 접근해서 질문 목록이 보이기까지 흐름에 대해 최대한 구체적으로 설명하라. 
* 


### 9. UserService와 QnaService 중 multi thread에서 문제가 발생할 가능성이 있는 소스는 무엇이며, 그 이유는 무엇인가?
* 
