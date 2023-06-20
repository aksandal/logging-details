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
        String id=Math.random()+"";
        MDC.put("groupId",id);
        if(MDC.get("stackId")==null){
            MDC.put("stackId",MDC.get("stackId")==null?"1":(MDC.get("stackId")+"1"));
        }
        MDC.put("status","Entry");
        log.info(String.format("%s",joinPoint.getSignature()));
        //MDC.put("stackId",MDC.get("stackId")==null?"1":(MDC.get("stackId")+"1"));
        Object result = joinPoint.proceed();
        MDC.put("groupId",id);
        MDC.put("status","Exit");
        //MDC.put("stackId",MDC.get("stackId").substring(0,MDC.get("stackId").length()-1));
        MDC.put("stackId",MDC.get("stackId")==null?"1":(MDC.get("stackId")+"1"));
        log.info(String.format("%s",joinPoint.getSignature()));
        return result;
/*        if (log.isDebugEnabled()) {
            log.debug("Enter: {}.{}() with argument[s] = {}", joinPoint.getSignature().getDeclaringTypeName(),
                    joinPoint.getSignature().getName(), Arrays.toString(joinPoint.getArgs()));
        }
        try {
            log.info(joinPoint.getSignature().toString());
            Object result = joinPoint.proceed();
            if (log.isDebugEnabled()) {
                log.debug("Exit: {}.{}() with result = {}", joinPoint.getSignature().getDeclaringTypeName(),
                        joinPoint.getSignature().getName(), result);
            }
            return result;
        } catch (IllegalArgumentException e) {
            log.error("Illegal argument: {} in {}.{}()", Arrays.toString(joinPoint.getArgs()),
                    joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName());
            throw e;
        }*/
    }
}
