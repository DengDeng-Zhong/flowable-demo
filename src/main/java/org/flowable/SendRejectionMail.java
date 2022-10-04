package org.flowable;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;

/**
 * @author DengBo_Zhong
 * @version V1.0
 * @date 2022/10/4 21:44
 */
public class SendRejectionMail implements JavaDelegate {
    @Override
    public void execute(DelegateExecution delegateExecution) {
        // 触发执行的逻辑，按照我们在流程中的定义应该给被拒绝的员工发通知的邮件
        System.out.println("审批被拒绝了呀……");
    }
}
