package com.app.sinkinchan.smartstock.activitys;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;

import com.app.sinkinchan.smartstock.R;
import com.app.sinkinchan.smartstock.activitys.base.BaseActivity;
import com.app.sinkinchan.smartstock.databinding.ActivityFeedbackBinding;
import com.app.sinkinchan.smartstock.utils.ToastUtil;
import com.app.sinkinchan.smartstock.utils.UserUtil;
import com.baidu.mobstat.StatService;

import org.apache.commons.lang3.StringUtils;

/**
 * @author : 陈欣健
 * @describe :
 * @since :2016-12-29 下午4:19
 **/
public class FeedbackActivity extends BaseActivity {
    ActivityFeedbackBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_feedback);
        init();

    }

    @Override
    protected void init() {
        setBackMenu();
        setTitle("用户反馈");
        binding.btnSend.setOnClickListener(view -> {
            String email = binding.edEmail.getText().toString();
            String message = binding.edMessage.getText().toString();
            if (StringUtils.isBlank(email) || StringUtils.isBlank(message)) {
                ToastUtil.showLongToast(getApplicationContext(), "邮箱或者内容不能为空.");
                return;
            }
            if (!UserUtil.isEmail(email)) {
                ToastUtil.showLongToast(getApplicationContext(), "请输入合法的邮箱");
                return;
            }
            StatService.onEvent(this, "user_feedback", "反馈邮箱: " + email + " 反馈内容:" + message, 1);
            new AlertDialog.Builder(FeedbackActivity.this)
                    .setTitle("提醒")
                    .setMessage("提交成功!作者会根据你的邮箱对你进行回复的.")
                    .setPositiveButton("确定", (dialog, index) -> {
                        dialog.dismiss();
                    })
                    .show();


        });

    }

    public void sendMail(String fromMail, String user, String password,
                         String toMail,
                         String mailTitle,
                         String mailContent) throws Exception {
        /*Properties props = new Properties(); //可以加载一个配置文件
        // 使用smtp：简单邮件传输协议
        props.put("mail.smtp.host", "smtp.163.com");//存储发送邮件服务器的信息
        props.put("mail.smtp.auth", "true");//同时通过验证

        Session session = Session.getInstance(props);//根据属性新建一个邮件会话
//        session.setDebug(true); //有他会打印一些调试信息。

        MimeMessage message = new MimeMessage(session);//由邮件会话新建一个消息对象
        message.setFrom(new InternetAddress(fromMail));//设置发件人的地址
        message.setRecipient(Message.RecipientType.TO, new InternetAddress(toMail));//设置收件人,并设置其接收类型为TO
        message.setSubject(mailTitle);//设置标题
        //设置信件内容
//        message.setText(mailContent); //发送 纯文本 邮件 todo
        message.setContent(mailContent, "text/html;charset=gbk"); //发送HTML邮件，内容样式比较丰富
        message.setSentDate(new Date());//设置发信时间
        message.saveChanges();//存储邮件信息

        //发送邮件
//        Transport transport = session.getTransport("smtp");
        Transport transport = session.getTransport();
        transport.connect(user, password);
        transport.sendMessage(message, message.getAllRecipients());//发送邮件,其中第二个参数是所有已设好的收件人地址
        transport.close();*/

    }

    @Override
    protected void registerListener() {

    }

    @Override
    protected void unRegisterListener() {

    }

}
