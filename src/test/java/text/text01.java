package text;

import org.junit.jupiter.api.Test;

import java.util.*;

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
        Map map = new HashMap();
        map.put("phone", "code");
        String code = (String) map.get("phone");
        System.out.println(code);
    }

    @Test
    public void test(List<Integer> list) {
        // 向list中添加元素
        Iterator<Integer> iterator = list.iterator();
        while (iterator.hasNext()) {
            Integer item = iterator.next();
            System.out.println(item);
            iterator.remove();
        }
    }

    @Test
    public byte test11(byte l,byte w) {
      return (byte) (l-w)
              ;
    }
}
