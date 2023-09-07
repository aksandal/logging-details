@Component
@Aspect
@Profile("dev")
public class LoggingAspect {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private static ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();

/*    @Before("applicationPackagePointcut()")
    public void secondAdvice(){
        System.out.println("Executing secondAdvice on getName()");
    }*/
    @Pointcut("within(com.example.firstrestapi..*)")
    public void applicationPackagePointcut() {
        // Method is empty as this is just a Pointcut, the implementations are in the advices.
    }
    @Pointcut("within(org.springframework.web.client..*)")
    public void pointcut() {

    }

    @Around("applicationPackagePointcut() || pointcut()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        if(Objects.isNull(MDC.get("groupId"))){
            String id=TestingUtil.randomStringGenerator();
            MDC.put("groupId",id);
        }
        if(Objects.isNull(MDC.get("globalStackId"))){
            MDC.put("globalStackId","1");
        }else{
            MDC.put("globalStackId",(Integer.parseInt(MDC.get("globalStackId"))+1)+"");
        }

        int stackId=Integer.parseInt(MDC.get("globalStackId"))+1;
        MDC.put("stackId",stackId+"");
        if(Objects.isNull(MDC.get("seqId"))) {
            MDC.put("seqId", "1");
        }else{
            MDC.put("seqId",(Integer.parseInt(MDC.get("seqId"))+1)+"");
        }
        MDC.put("status","Entry");
        log.info(String.format("%s",joinPoint.getSignature()));
        Object result = joinPoint.proceed();
        MDC.put("status","Exit");
        MDC.put("stackId",stackId+"");
        MDC.put("seqId",(Integer.parseInt(MDC.get("seqId"))+1)+"");
        log.info(String.format("%s",joinPoint.getSignature()));
        return result;
    }

    public static String  randomStringGenerator(){
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 10;
        Random random = new Random();

        return random.ints(leftLimit, rightLimit + 1)
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }
}
