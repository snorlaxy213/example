package easyexcel;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.fastjson.JSON;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Easy Excel User解析
 *
 * @author Chen Jiaying
 */
public class UserListener extends AnalysisEventListener<User> {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserListener.class);

    /**
     * 每隔5条存储数据库，实际使用中可以3000条，然后清理list ，方便内存回收
     */
    private static final int BATCH_COUNT = 3000;
    /**
     * 缓存的数据
     */
    private List<User> list = new ArrayList<>(BATCH_COUNT);

    public UserListener() {
    }

    public UserListener(List<User> list) {
        this.list = list;
    }

    /**
     * 这个每一条数据解析都会来调用
     *
     * @param data    one row value. It is same as {@link AnalysisContext#readRowHolder()}
     * @param context
     */
    @Override
    public void invoke(User data, AnalysisContext context) {
        LOGGER.info("解析到一条数据:{}", JSON.toJSONString(data));
        list.add(data);
        // 达到BATCH_COUNT了，需要去存储一次数据库，防止数据几万条数据在内存，容易OOM
//        if (list.size() >= BATCH_COUNT) {
//            // 存储完成清理 list
//            list = new ArrayList<>(BATCH_COUNT);
//        }
    }

    /**
     * 打印表头
     *
     * @param headMap 表头
     * @param context
     */
    @Override
    public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {
        LOGGER.info("表头:{}", headMap);
    }

    /**
     * 所有数据解析完成了 都会来调用
     *
     * @param context
     */
    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {

        System.out.println("每3K进行一次打印");

        LOGGER.info("所有数据解析完成！");
    }
}