### 2. 서버가 시작하는 시점에 부모 ApplicationContext와 자식 ApplicationContext가 초기화되는 과정에 대해 구체적으로 설명해라.
- 톰캣이 시작되면, 가장 먼저 web.xml을 읽는다.
- web.xml에 <listener>로 org.springframework.web.context.ContextLoaderListener 가 등록되어 있으므로, ContextLoaderListener의 contextInitialized 메소드를 실행한다.
- 이 메소드 안에서 initWebApplicationContext(event.getServletContext()); 를 실행한다. 이 때, web.xml의 contextParam으로 applicationContext.xml의 path가 저장되어 있다. 따라서 applicationContext를 읽어 부모 스프링 컨테이너를 만든다.
- loadOnStartUp 설정이 되어 있지 않으므로, 처음 요청이 올 때, DispatcherServlet이 초기화된다.
- 초기화 되는 과정은 다음과 같다. DispatcherServlet의 부모 클래스인 HttpServletBean의 init()메소드가 먼저 실행되게 된다. 이 init() 메소드 안에서 initServletBean() 메소드를 호출한다. initServletBean 메소드는 FrameworkServlet 클래스에서 구현되어 있다. 구현부분을 살펴보면, initServletBean() 안에서 createWebApplicationContext(rootContext)를 실행한다. (이 이상은 더이상 자세히 설명하지 않겠다.) 이 안에서 next-servlet.xml을 읽어 앞서 만든 부모 스프링 컨테이너를 부모로 갖는 자식 스프링 컨테이너가 만들어지게 된다. 

### 3. 서버 시작 후 http://localhost:8080으로 접근해서 질문 목록이 보이기까지 흐름에 대해 최대한 구체적으로 설명하라. 
* 서블릿으로 DispatcherServlet이 등록되어 있으므로, 모든 요청은 DispatcherServlet의 doService() 메소드로 들어온다. 
* doService()안에서 doDispatch() 메소드를 호출한다. doDipatch메소드 안에서 ModelAndView 타입의 mv 를 초기화한다. 이 때 mv를 초기화하며   uri가 ""로 맵핑되어 있는 QuestionController의 list 메소드가 실행되는 것 같다. 따라서 mv에는 model로 qna/list.jsp가 등록되게 된다. 
* 이 후 processDispatchResult에 request와 response, mv 등을 파라미터로 넘겨 실행한다. 
* 이 processDispatchResult()에서 qna/list.jsp 를 render 한다.   

### 9. UserService와 QnaService 중 multi thread에서 문제가 발생할 가능성이 있는 소스는 무엇이며, 그 이유는 무엇인가?
* UserService, QnaService 모두 문제가 발생할 가능성이 있다. 
* 먼저, UserService는 scope가 특별히 지정되어 있지 않으므로, singleton으로 관리된다. 그리고 existedUser는 login 메소드에서 로그인이 성공하면 초기화되게 된다. 그런데 많은 유저가 로그인을 하게 되면, UserService가 싱글톤이므로 가장 마지막에 로그인한 유저의 정보만이 existedUser에 저장된다. 따라서 먼저 로그인한 사람은 로그인을 제대로 했음에도 불구하고 다른 사람의 계정으로 로그인될 수 있다. 따라서 필드에 existedUser를 저장하지 말고, existedUser를 사용하는 메소드 안의 local변수로 existedUser를 저장하면, 문제를 해결할 수 있다.
* QnaService는 프로토타입 스코프로 설정되어 있지만, QnaService를 주입받는 QnaController는 프로토타입 스코프가 아니므로, QnaService 객체는 요청이 올때마다 새로 만들어지지 않고, 한번만 만들어지게 된다. 따라서 QestionController역시 프로토타입 스코프로 바꿔주어야 QnaService가 요청이 올 때마다 새로 만들어 질 수 있다. 하지만 이 방법은 성능이슈가 있다 따라서 더 좋은 방법으로는, UserService와 마찬가지로 프로토타입 스코프를 쓰지 않고, 필드 변수를 각 메소드의 로컬변수로 바꿔주면 문제를 해결할 수 있다. 

