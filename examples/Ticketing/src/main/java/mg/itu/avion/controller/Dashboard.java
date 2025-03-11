package mg.itu.avion.controller;

import com.ETU1792.annotation.Authentified;
import com.ETU1792.annotation.Controller;
import com.ETU1792.annotation.GET;
import com.ETU1792.annotation.Role;
import com.ETU1792.utils.ModelView;

@Controller
@Authentified
@Role({"USER", "ADMIN"})
public class Dashboard {
    @GET("dashboard")
    public ModelView getDashboard() {
        return new ModelView("/test.jsp");
    }
}
