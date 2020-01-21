package app;

/**
 * Test class for testing methods in {@link SessionHandler}
 * the login and manage methods there are fundamental for the web-session and login functionality.
 * The tests are run in the regular maven "test" phase where every Class is scanned for @Test annotation.
 * All these methods are executed as separate test.
 * <p>
 * In order to use mocks with Mockito JUnit is extended via a plugin in the @ExtendedWith annotation.
 */
@ExtendsWith(MockitoExtension.class)
public class TestSessionHandler {

    // the quserRepository will be automatically mocked (@Mock annotation)
    @Mock
    private QuserRepository quserRepository;

    // @InjectMocks tells Mockito to put the quserRespository mock as a constructor arg and
    // to instantiate our sessionHandler that shoul be tested
    @InjectMocks
    private SessionHandler sessionHandler;

    /**
     * tests the {@link SessionHandler#manage} method
     * scenario: grant access to "/login" path regardless if a user is authenticated
     */
    @Test
    public void testManageLoginPath() {
        MockitoAnnotations.initMocks(this);
        Context ctx = mock(Context.class);
        Handler handler = mock(Handler.class);
        Set<Role> roles = new HashSet<>();
        Mockito.lenient().when(ctx.path()).thenReturn("/login");
        try {
            sessionHandler.manage(handler, ctx, roles);
        } catch (Exception e) {
            fail("exception occurred in sessionhandler#manage", e);
        }
        try {
            verify(handler).handle(ctx);
        } catch (Exception e) {
            fail("Exception occurred in handlers handle call", e);
        }
    }

    /**
     * tests the {@link SessionHandler#manage} method
     * scenario: render the login page if the user is not authenticated in current web-session
     */
    @Test
    public void testManageNoSession() {
        MockitoAnnotations.initMocks(this);
        Context ctx = mock(Context.class);
        Handler handler = mock(Handler.class);
        Set<Role> roles = new HashSet<>();
        Mockito.lenient().when(ctx.path()).thenReturn("/foo");
        Mockito.lenient().when(ctx.sessionAttribute(eq("currentuser"))).thenReturn(null);
        Mockito.lenient().when(ctx.status(eq(401))).thenReturn(ctx);
        try {
            sessionHandler.manage(handler, ctx, roles);
        } catch (Exception e) {
            fail("exception occurred in sessionhandler#manage", e);
        }
        try {
            verify(ctx).status(eq(401));
            verify(ctx).redirect(eq("/login"));
            verify(handler, never()).handle(ctx);
        } catch (Exception e) {
            fail("Exception occurred in handlers handle call", e);
        }
    }

    /**
     * tests the {@link SessionHandler#manage} method
     * scenario: grant access to restricted resources if user is authenticated
     */
    @Test
    public void testManageWithSession() {
        MockitoAnnotations.initMocks(this);
        Context ctx = mock(Context.class);
        Handler handler = mock(Handler.class);
        Set<Role> roles = new HashSet<>();
        Mockito.lenient().when(ctx.path()).thenReturn("/foo");
        Mockito.lenient().when(ctx.sessionAttribute(eq("currentuser"))).thenReturn("Homer");
        try {
            sessionHandler.manage(handler, ctx, roles);
        } catch (Exception e) {
            fail("exception occurred in sessionhandler#manage", e);
        }
        try {
            verify(handler).handle(eq(ctx));
        } catch (Exception e) {
            fail("Exception occurred in handlers handle call", e);
        }
    }

    /**
     * tests the {@link SessionHandler#handleLogin} method
     * scenario: authenticate user
     * <ul>
     *     <li>username is provided with correct value</li>
     *     <li>password is provided with correct value</li>
     *     <li>user is present and returned complete from database</li>
     * </ul>
     * expected result: redirect browser to root path of app
     */
    @Test
    public void testHandleLoginSuccess() {
        MockitoAnnotations.initMocks(this);
        Context ctx = mock(Context.class);
        Mockito.lenient().when(ctx.formParam("username")).thenReturn("Homer");
        Mockito.lenient().when(ctx.formParam("password")).thenReturn("HomerJ");
        Quser dbUser = new Quser();
        dbUser.setUsername("Homer");
        dbUser.setPassword("b22e5919c6ca0bfc1c9817052a284364f718f9f7500710a44253dac27de5baad");
        Mockito.lenient().when(quserRepository.getUser(eq("Homer"))).thenReturn(dbUser);
        sessionHandler.handleLogin(ctx);
        verify(ctx).sessionAttribute(eq("currentuser"), eq("Homer"));
        verify(ctx).redirect("/");
    }

    /**
     * tests the {@link SessionHandler#handleLogin} method
     * scenario: authenticate user
     * <ul>
     *     <li>username is provided with correct value</li>
     *     <li>password is provided with incorrect value</li>
     *     <li>user is present and returned complete from database</li>
     * </ul>
     * expected result: return status "401 unauthorized"
     */
    @Test
    public void testHandleLoginWrongPassword() {
        MockitoAnnotations.initMocks(this);
        Context ctx = mock(Context.class);
        Mockito.lenient().when(ctx.formParam("username")).thenReturn("Homer");
        Mockito.lenient().when(ctx.formParam("password")).thenReturn("HomerJ");
        Mockito.lenient().when(ctx.status(anyInt())).thenReturn(ctx);
        Quser dbUser = new Quser();
        dbUser.setUsername("Homer");
        dbUser.setPassword("b22e5919c6ca0bfc1c9817052a284364f718f9f7500710a44253dac27de5baaw");
        Mockito.lenient().when(quserRepository.getUser(eq("Homer"))).thenReturn(dbUser);
        sessionHandler.handleLogin(ctx);
        verify(ctx, never()).sessionAttribute(eq("currentuser"), eq("Homer"));
        verify(ctx).status(eq(401));
        verify(ctx).html(eq("Access Denied"));
    }

    /**
     * tests the {@link SessionHandler#handleLogin} method
     * scenario: authenticate user
     * <ul>
     *     <li>username is provided with correct value</li>
     *     <li>password is provided with correct value</li>
     *     <li>user is present and returned with wrong username from database</li>
     * </ul>
     * expected result: return status "401 unauthorized"
     */
    @Test
    public void testHandleLoginWrongDatabaseResult() {
        MockitoAnnotations.initMocks(this);
        Context ctx = mock(Context.class);
        Mockito.lenient().when(ctx.formParam("username")).thenReturn("Homer");
        Mockito.lenient().when(ctx.formParam("password")).thenReturn("HomerJ");
        Mockito.lenient().when(ctx.status(anyInt())).thenReturn(ctx);
        Quser dbUser = new Quser();
        dbUser.setUsername("Ned");
        dbUser.setPassword("b22e5919c6ca0bfc1c9817052a284364f718f9f7500710a44253dac27de5baad");
        Mockito.lenient().when(quserRepository.getUser(eq("Homer"))).thenReturn(dbUser);
        sessionHandler.handleLogin(ctx);
        verify(ctx, never()).sessionAttribute(eq("currentuser"), eq("Homer"));
        verify(ctx).status(eq(401));
        verify(ctx).html(eq("Access Denied"));
    }

    /**
     * tests the {@link SessionHandler#handleLogin} method
     * scenario: authenticate user
     * <ul>
     *     <li>username is provided with correct value</li>
     *     <li>password is provided with incorrect value</li>
     *     <li>user is present but has no password in database</li>
     * </ul>
     * expected result: return status "401 unauthorized"
     */
    @Test
    public void testHandleLoginDatabaseResultContainingNull() {
        MockitoAnnotations.initMocks(this);
        Context ctx = mock(Context.class);
        Mockito.lenient().when(ctx.formParam("username")).thenReturn("Homer");
        Mockito.lenient().when(ctx.formParam("password")).thenReturn("HomerJ");
        Mockito.lenient().when(ctx.status(anyInt())).thenReturn(ctx);
        Quser dbUser = new Quser();
        dbUser.setUsername("Homer");
        Mockito.lenient().when(quserRepository.getUser(eq("Homer"))).thenReturn(dbUser);
        sessionHandler.handleLogin(ctx);
        verify(ctx, never()).sessionAttribute(eq("currentuser"), eq("Homer"));
        verify(ctx).status(eq(401));
        verify(ctx).html(eq("Access Denied"));
    }

    /**
     * tests the {@link SessionHandler#handleLogin} method
     * scenario: authenticate user
     * <ul>
     *     <li>username is provided with correct value</li>
     *     <li>password is provided with correct value</li>
     *     <li>user is not present in database</li>
     * </ul>
     * expected result: return status "401 unauthorized"
     */
    @Test
    public void testHandleLoginUnknownUser() {
        MockitoAnnotations.initMocks(this);
        Context ctx = mock(Context.class);
        Mockito.lenient().when(ctx.formParam("username")).thenReturn("unknown");
        Mockito.lenient().when(ctx.formParam("password")).thenReturn("HomerJ");
        Mockito.lenient().when(ctx.status(anyInt())).thenReturn(ctx);
        Mockito.lenient().when(quserRepository.getUser(eq("unknown"))).thenReturn(null);
        sessionHandler.handleLogin(ctx);
        verify(ctx, never()).sessionAttribute(eq("currentuser"), eq("unknown"));
        verify(ctx).status(eq(401));
        verify(ctx).html(eq("Access Denied"));
    }

    /**
     * tests the {@link SessionHandler#handleLogin} method
     * scenario: authenticate user
     * <ul>
     *     <li>username is provided with correct value</li>
     *     <li>password is not provided</li>
     * </ul>
     * expected result: render login form again in order to try login again
     */
    @Test
    public void testHandleLoginEmptyPassword() {
        MockitoAnnotations.initMocks(this);
        Context ctx = mock(Context.class);
        Mockito.lenient().when(ctx.formParam("username")).thenReturn("Homer");
        Mockito.lenient().when(ctx.formParam("password")).thenReturn("");
        Mockito.lenient().when(ctx.status(anyInt())).thenReturn(ctx);
        sessionHandler.handleLogin(ctx);
        verify(quserRepository, never()).getUser(anyString());
        verify(ctx, never()).sessionAttribute(eq("currentuser"), eq("Homer"));
        verify(ctx).render(endsWith("login.html"));
    }

    /**
     * tests the {@link SessionHandler#handleLogin} method
     * scenario: authenticate user
     * <ul>
     *     <li>username is not provided</li>
     *     <li>password is not provided</li>
     * </ul>
     * expected result: render login form again in order to try login again
     */
    @Test
    public void testHandleLoginEmptyForm() {
        MockitoAnnotations.initMocks(this);
        Context ctx = mock(Context.class);
        Mockito.lenient().when(ctx.formParam("username")).thenReturn("");
        Mockito.lenient().when(ctx.formParam("password")).thenReturn("");
        Mockito.lenient().when(ctx.status(anyInt())).thenReturn(ctx);
        sessionHandler.handleLogin(ctx);
        verify(quserRepository, never()).getUser(anyString());
        verify(ctx, never()).sessionAttribute(eq("currentuser"), eq(""));
        verify(ctx).render(endsWith("login.html"));
    }

}