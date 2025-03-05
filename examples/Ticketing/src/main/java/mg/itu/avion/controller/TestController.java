package mg.itu.avion.controller;

import com.ETU1792.annotation.Controller;
import com.ETU1792.annotation.GET;
import com.ETU1792.utils.ModelView;

@Controller
public class TestController {
    @GET(value = "test")
    public ModelView test() {
        return new ModelView("test.jsp");
    }
}
