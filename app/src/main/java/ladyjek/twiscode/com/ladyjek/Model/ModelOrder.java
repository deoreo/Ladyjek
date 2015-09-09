package ladyjek.twiscode.com.ladyjek.Model;

/**
 * Created by Unity on 02/09/2015.
 */
public class ModelOrder {
    private String id, to, distance, duration,status,from,userID,toLongitude,toLatitude,fromLongitude,fromLatitude, name, rate, phone, member, payment, price;

    public ModelOrder(){

    }
    public ModelOrder(String id, String userID, String name, String to, String from, String distance, String duration, String status, String toLongitude, String toLatitude, String fromLatitude, String fromLongitude, String rate, String phone, String member, String payment, String price){
        this.id = id;
        this.userID = userID;
        this.name = name;
        this.to = to;
        this.from = from;
        this.distance = distance;
        this.duration = duration;
        this.status = status;
        this.toLatitude = toLatitude;
        this.toLongitude = toLongitude;
        this.fromLatitude = fromLatitude;
        this.fromLongitude = fromLongitude;
        this.rate = rate;
        this.phone = phone;
        this.member = member;
        this.payment = payment;
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getToLongitude() {
        return toLongitude;
    }

    public void setToLongitude(String toLongitude) {
        this.toLongitude = toLongitude;
    }

    public String getToLatitude() {
        return toLatitude;
    }

    public void setToLatitude(String toLatitude) {
        this.toLatitude = toLatitude;
    }

    public String getFromLongitude() {
        return fromLongitude;
    }

    public void setFromLongitude(String fromLongitude) {
        this.fromLongitude = fromLongitude;
    }

    public String getFromLatitude() {
        return fromLatitude;
    }

    public void setFromLatitude(String fromLatitude) {
        this.fromLatitude = fromLatitude;
    }

    public String getToSmall(){
        String[] separated = to.split(",");
        return separated[0];
    }

    public String getFromSmall(){
        String[] separated = from.split(",");
        return separated[0];
    }

    public void setRate(String rate){
        this.rate = rate;
    }

    public String getRate(){
        return this.rate;
    }

    public void setPhone(String data){
        this.phone = data;
    }

    public String getPhone(){
        return this.phone;
    }

    public void setMember(String data){
        this.member = data;
    }

    public String getMember(){
        return this.member;
    }

    public void setPayment(String data){
        this.payment = data;
    }

    public String getPayment(){
        return this.payment;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
