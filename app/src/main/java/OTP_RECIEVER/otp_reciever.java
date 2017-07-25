//package OTP_RECIEVER;
//
///**
// * Created by Mr singh on 2/3/2017.
// */
//
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
//import android.os.Bundle;
//import android.telephony.SmsManager;
//import android.telephony.SmsMessage;
//import android.util.Log;
//
//import in.co.mealman.mealman.Screen_03;
//
//public class otp_reciever extends BroadcastReceiver
//{
//    @Override
//    public void onReceive(Context context, Intent intent)
//    {
//
//        final Bundle bundle = intent.getExtras();
//        try {
//
//            if (bundle != null)
//            {
//
//                final Object[] pdusObj = (Object[]) bundle.get("pdus");
//                for (int i = 0; i < pdusObj.length; i++)
//                {
//
//                    SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
//                    String phoneNumber = currentMessage.getDisplayOriginatingAddress();
//                    String senderNum = phoneNumber ;
//                    String message = currentMessage .getDisplayMessageBody();
//
//                    try
//                    {
//
//                        if (senderNum.equals("VK-MEALMN"))
//                        {
//
//                            Screen_03 Sms = new Screen_03();
//                            Sms.recivedSms(message );
//                        }
//                    } catch(Exception e){}
//                }
//            }
//
//        } catch (Exception e) {}
//    }
//
//}