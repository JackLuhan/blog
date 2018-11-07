/**
 * Copyright [2016-2017] [houyi]
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.houyi.blog.spider.scheduler;

import com.houyi.blog.spider.enums.ExitWayEnum;
import com.houyi.blog.spider.model.BaseModel;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.scheduler.DuplicateRemovedScheduler;
import us.codecraft.webmagic.scheduler.MonitorableScheduler;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 自定义的调度器，主要用来处理url
 *
 * @author houyi
 * @version 1.0

 * @date 2018/7/31 17:37
 */
public class BlockingQueueScheduler extends DuplicateRemovedScheduler implements MonitorableScheduler {
    private BlockingQueue<Request> queue = new LinkedBlockingQueue<>();
    private int realUrlCount = -1;

    public BlockingQueueScheduler(BaseModel model) {
        if (ExitWayEnum.URL_COUNT.toString().equals(model.getExitWay())) {
            // 实际抓取的url数量包括入口页面
            this.realUrlCount = model.getCount() + model.getEntryUrls().length;
        }
    }

    @Override
    public void pushWhenNoDuplicate(Request request, Task task) {
        // 当程序退出方式非URL_COUNT时按照正常逻辑处理
        if (realUrlCount == -1) {
            this.queue.add(request);
            return;
        }
        // 在有效期内(realUrlCount > 0)，每次push url时realUrlCount - 1， 当 realUrlCount <= 0 时，当前Scheduler将不再收录新的url
        if (realUrlCount <= 0) {
            return;
        }
        realUrlCount--;
        this.queue.add(request);
    }

    @Override
    public Request poll(Task task) {
        return (Request) this.queue.poll();
    }

    @Override
    public int getLeftRequestsCount(Task task) {
        return this.queue.size();
    }

    @Override
    public int getTotalRequestsCount(Task task) {
        return this.getDuplicateRemover().getTotalRequestsCount(task);
    }
}
