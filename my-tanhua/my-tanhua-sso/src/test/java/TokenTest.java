import com.tanhua.commons.utils.TokenUtil;
import com.tanhua.sso.SSOApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = SSOApplication.class)
public class TokenTest {

    @Test
    public void test1() {
//        String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJtb2JpbGUiOiIxODkxMjM0MTIzNCIsImlkIjoiMTQyIn0.0lk_cQ4iNZg3GWiWAWakAGq8lqF-DN3Sgp0gJZ-naRk";
//        String secret = "76bd425b6f29f7fcc2e0bfc286043df1";
//        Map<String, Object> map = TokenUtil.parseToken(token, secret);
//        System.out.println(map);
    }

    @Test
    public void test2() {
        int[] nums = {1, 2, 3, 6};
        for (int i = 0; i < nums.length; i++) {
            int x = 9 - nums[i];
            for (int j = i; j < nums.length; j++) {
                if (nums[j] == x) {
                    System.out.println(i);
                    System.out.println(j);
                }
            }
        }
    }

    @Test
    public void test3() {
        String aa = "      ";
        System.out.println(aa.length());
    }

}
