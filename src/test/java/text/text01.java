package text;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * @author : Bojack
 * @date : Created in 16:17 2023.02.08
 */
public class text01 {
    @Test
    public void spile() {
        String name = "qdqed.qe.wqd.ed.efw.ef";
        String[] split = name.split("\\.");
        for (String n :
                split) {
            System.out.println(n);
        }
    }

    @Test
    public void len() {
        Map map=new HashMap();
        map.put("phone","code");
        String code = (String) map.get("phone");
        System.out.println(code);
    }
}
