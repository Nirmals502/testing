package in.co.mealman.mealman.Service_handler;

public interface Constant {

    String Server = "http://mealman.roshi.in/";
    String Server2 = "http://34.197.228.70/";
    String USER_PROFILE = Server2 + "api/login/getUserProfile";
    String UPDATE_PROFILE = Server2 + "api/login/updateUserAfterSignup";
    String Get_Subscription = Server2 + "api/order/getSubscriptionPackage";
    String Get_Testimonial = Server2 + "api/testimonial/getTestimonial";
    String Add_Order = "http://34.197.228.70/api/order/addCart";
    String Update_profile_pic = Server2 + "api/login/updateUserProfilePic";
    String UPDATE_PASSWORD = Server2 + "api/login/updateUserPassword";
    String UPDATE_USER_PROFILE = Server2 + "api/login/updateUserFromProfile";
    String ADD_TESTIMONIAL = Server2 + "api/testimonial/addTestimonial";
    String GET_USER_PROFILE = Server2 + "/api/login/getProfile";
    String join = Server2 + "/api/login/createNewUser";
    String RESEND_OTP = Server2 + "api/login/resendOTP";
    String GET_ORDERLIST = Server2 + "api/order/getMealForDate";
    String GET_BANNER = Server2 + "api/order/getBanner";
    String ORDER_HISTORY = Server2 + "api/order/getMeal";
    String ORDER_CART = Server2 + "api/order/getAllCart";
    String DELETE_CART = Server2+ "api/order/deleteCart";
    String FORGOT_PASSWORD = Server2 + "api/login/resendOTPWithMobile";
    String UPDATE_ORDER = Server2 + "api/order/addOrder";
   // http://34.197.228.70/api/order/addOrder?userID=3&paymentID=3434&totalAmount=1200


}