import com.alibaba.fastjson.JSON;
import com.tanhua.commons.constants.RedisKey;
import com.tanhua.sso.SSOApplication;
import org.apache.commons.codec.digest.DigestUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Map;
import java.util.concurrent.TimeUnit;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = SSOApplication.class)
public class TokenTest {

//    @Autowired
//    private HuanXinTokenService huanXinTokenService;
//
//    @Autowired
//    private RedisTemplate<String, String> redisTemplate;

//    @Autowired
//    private HuanXinService huanXinService;

//    @Test
//    public void test1() {
//        String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJtb2JpbGUiOiIxODkxMjM0MTIzNCIsImlkIjoiMTQyIn0.0lk_cQ4iNZg3GWiWAWakAGq8lqF-DN3Sgp0gJZ-naRk";
//        String secret = "76bd425b6f29f7fcc2e0bfc286043df1";
//        Map<String, Object> map = TokenUtil.parseToken(token, secret);
//        System.out.println(map);
//    }

//    @Test
//    public void test2() {
//        int[] nums = {1, 2, 3, 6};
//        for (int i = 0; i < nums.length; i++) {
//            int x = 9 - nums[i];
//            for (int j = i; j < nums.length; j++) {
//                if (nums[j] == x) {
//                    System.out.println(i);
//                    System.out.println(j);
//                }
//            }
//        }
//    }
//
//    @Test
//    public void test3() {
//        String aa = "      ";
//        System.out.println(aa.length());
//    }

//    @Test
//    public void test4() {
//        String token = huanXinTokenService.getHuanXinToken();
//        System.out.println(token);
//    }

//    @Test
//    public void test5() {
//        String str = "{\"application\":\"87fcf067-fda6-48a1-b428-6309b4a1728f\",\"access_token\":\"YWMtd5ZSnlLkEeum8mG3dVxh8gAAAAAAAAAAAAAAAAAAAAGH_PBn_aZIobQoYwm0oXKPAgMAAAF26fFb4QBPGgCWOTEYYQFq000MEGyps5BJdtXTweIocNhRIqjk919LMg\",\"expires_in\":5184000}\n";
//        YWMtd5ZSnlLkEeum8mG3dVxh8gAAAAAAAAAAAAAAAAAAAAGH_PBn_aZIobQoYwm0oXKPAgMAAAF26fFb4QBPGgCWOTEYYQFq000MEGyps5BJdtXTweIocNhRIqjk919LMg
//        87fcf067-fda6-48a1-b428-6309b4a1728f
//        5184000
//
//        System.out.println(parseObject);
//    }

//    @Test
//    public void test6() {
//        String redisKey = RedisKey.HUANXIN;
//        String access_token = "YWMtd5ZSnlLkEeum8mG3dVxh8gAAAAAAAAAAAAAAAAAAAAGH_PBn_aZIobQoYwm0oXKPAgMAAAF26fFb4QBPGgCWOTEYYQFq000MEGyps5BJdtXTweIocNhRIqjk919LMg";
//        Long expires_in = 5184000L;
//        redisTemplate.opsForValue().set(redisKey, access_token, (expires_in - 36000), TimeUnit.SECONDS);
//    }
//
//    @Test
//    public void test7() {
//        String redisKey = RedisKey.HUANXIN;
//        System.out.println(redisTemplate.opsForValue().get(redisKey));
//    }

//    @Test
//    public void test8() {
//        String s = huanXinService.registerUser(95L);
//        System.out.println(s);
//    }

    // {duration=85, path=/users, application=87fcf067-fda6-48a1-b428-6309b4a1728f, entities=[{"created":1610255105121,"modified":1610255105121,"type":"user","uuid":"66e2bba0-5301-11eb-9372-27024c5eae94","username":"95","activated":true}], organization=1116210104148369, action=post, uri=https://a1.easemob.com/1116210104148369/test/users, applicationName=test, timestamp=1610255105079}
    // {"path":"/users","uri":"https://a1.easemob.com/1116210104148369/test/users","timestamp":1610255237973,"organization":"1116210104148369","application":"87fcf067-fda6-48a1-b428-6309b4a1728f","entities":[{"uuid":"b615d950-5301-11eb-b309-1b46121a9c68","type":"user","created":1610255237996,"modified":1610255237996,"username":"95","activated":true}],"action":"post","duration":67,"applicationName":"test"}

    @Test
    public void test9() {
        System.out.println(DigestUtils.md5Hex(String.valueOf(1)));
    }

    // 2 --- c81e728d9d4c2f636f067f89cc14862c
    // 1 --- c4ca4238a0b923820dcc509a6f75849b
}
