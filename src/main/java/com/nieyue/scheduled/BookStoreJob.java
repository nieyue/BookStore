package com.nieyue.scheduled;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.BoundSetOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.nieyue.bean.Book;
import com.nieyue.bean.BookCate;
import com.nieyue.bean.BookChapter;
import com.nieyue.service.BookCateService;
import com.nieyue.service.BookChapterService;
import com.nieyue.service.BookService;
import com.nieyue.util.HttpClientUtil;
import com.nieyue.util.NumberUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


@Component
public class BookStoreJob {
	@Resource
	StringRedisTemplate stringRedisTemplate;
	@Value("${myPugin.projectName}")
	String projectName;
	@Resource
	BookService bookService;
	@Resource
	BookCateService bookCateService;
	@Resource
	BookChapterService bookChapterService;
	//书单获取URL
	public final static String YUE_CHENG_SHU_BA_BOOKORDER_URL ="http://116.62.189.118:7380/channelman/ws/rest/selectChannelBookList";
	//单个书籍URL
	public final static String YUE_CHENG_SHU_BA_BOOK_URL ="http://116.62.189.118:7680/bookyunying/ws/rest/yfb";
	//渠道ID
	public final static String YUE_CHENG_SHU_BA_channelidTwo ="118";
	//渠道KEY
	public final static String YUE_CHENG_SHU_BA_KEY = "HE9G9H727W67WGGS09D99G8M1";
	public final static long ONE_Minute =   20*1000;
	 public final static long TEN_SECONDS =   10*1000;
	 /**
	  * 获取悦诚书吧书单Sign
	  * @return
	  */
	 public  String getYueChengShuBaBookOrderSign(){
		return DigestUtils.md5Hex(YUE_CHENG_SHU_BA_channelidTwo+YUE_CHENG_SHU_BA_KEY).substring(8, 24).toUpperCase();
	 }
	 /**
	  * 获取悦诚书吧书单列表
	  * @return
	  */
	 public  String getYueChengShuBaBookOrderList(){
		 String json="{\"channelidTwo\":\""+YUE_CHENG_SHU_BA_channelidTwo+"\",\"sign\":\""+getYueChengShuBaBookOrderSign()+"\"}";
		 String s = HttpClientUtil.doPostJson(YUE_CHENG_SHU_BA_BOOKORDER_URL,json);
			return s;
	 }
	 /**
	  * 获取悦诚书吧书单书id数组
	  * @return
	  */
	 public  Integer[] getYueChengShuBaBookIdArray(){
		 	String s = getYueChengShuBaBookOrderList();
			JSONObject bookOrderWrapJson=JSONObject.fromObject(s);
			Object dataList = bookOrderWrapJson.get("data");
			JSONArray bookOrderDataJSONArray=JSONArray.fromObject(dataList);
			Integer[] ss=new Integer[ bookOrderDataJSONArray.size()];
			for (int i = 0; i < bookOrderDataJSONArray.size(); i++) {
				JSONObject bookOrderJson=JSONObject.fromObject(bookOrderDataJSONArray.get(i));
				ss[i]=bookOrderJson.getInt("bookid");
			}
			return ss;
	 }
	 
	 /**
	  * 获取悦诚书吧书Sign
	  * @return
	  */
	 public  String getYueChengShuBaBookSign(Integer bookId){
		return DigestUtils.md5Hex(bookId+YUE_CHENG_SHU_BA_channelidTwo+YUE_CHENG_SHU_BA_KEY).substring(8, 24).toUpperCase();
	 }
	 /**
	  * 获取悦诚书吧书详情
	  * @return
	 * @throws Exception 
	  */
	 public  String getYueChengShuBaBookDetails(Integer bookId) throws Exception{
		 String sign=getYueChengShuBaBookSign( bookId);
		 String s = HttpClientUtil.doGet(YUE_CHENG_SHU_BA_BOOK_URL+"/"+bookId+"/"+YUE_CHENG_SHU_BA_channelidTwo+"/"+sign);
			return s;
	 }
	 /**
	  * 获取悦诚书吧书列表
	  * @return
	 * @throws Exception 
	  */
	 public  List<Book> getYueChengShuBaBook(Integer bookId) throws Exception{
		 BoundSetOperations<String, String> jsadbzso=stringRedisTemplate.boundSetOps(projectName+"BookStoreYueChengShuBaBook");
		 int oldSize = jsadbzso.members().size();
  	   jsadbzso.add(bookId.toString());
  	   int nowSize = jsadbzso.members().size();
  	   	if(nowSize==oldSize){//相等就是没有增加
  	   		return new ArrayList<Book>();
  	   	}
		 	List<Book> bookList=new ArrayList<Book>();
		 	String s = getYueChengShuBaBookDetails(bookId);
			JSONObject bookWrapJson=JSONObject.fromObject(s);
			Object dataList = bookWrapJson.get("data");
			JSONArray bookDataJSONArray=JSONArray.fromObject(dataList);
			for (int i = 0; i < bookDataJSONArray.size(); i++) {
				JSONObject bookJson=JSONObject.fromObject(bookDataJSONArray.get(i));
				Book book = new Book();
				book.setAuthor(bookJson.getString("author"));//作者
				book.setImgAddress(bookJson.getString("bookcover"));//图片
				book.setTitle(bookJson.getString("bookname"));//名称
				book.setRedirectUrl(bookJson.getString("url"));//跳转url
				book.setSummary(bookJson.getString("mess"));//简介
				if(bookJson.getString("sex").equals("2")){//女频
					book.setRecommend(6);;//女频
				}else if(bookJson.getString("sex").equals("1")){
					book.setRecommend(5);;//男频
				}else{
					book.setRecommend(0);;//其他
					
				}
				BookCate bookCate = new BookCate();
				//类型名称
				bookCate.setName(bookJson.getString("type").substring(0, bookJson.getString("type").indexOf(",")));
				book.setBookCate(bookCate);
				BookCate bc = bookCateService.loadBookCate(null, bookCate.getName());
				if(bc==null||bc.equals("")){//该类型不存在，则存储
					bookCateService.addBookCate(bookCate);
					book.setBookCateId(bookCate.getBookCateId());
				}else{
					book.setBookCateId(bc.getBookCateId());
				}
				bookList.add(book);
				bookService.addBook(book);
				//书章节
				List<BookChapter> bookChapterList=new ArrayList<BookChapter>();
				String bookChapterListString = bookJson.getString("catalogue");
				JSONArray bookChapterListStringJSONArray=JSONArray.fromObject(bookChapterListString);
				for (int j = 0; j < bookChapterListStringJSONArray.size(); j++) {
					JSONObject bookChapterJson=JSONObject.fromObject(bookChapterListStringJSONArray.get(j));
					BookChapter bookChapter=new BookChapter();
					//章节
					String bookchapterNumber = bookChapterJson.getString("cataloguename").substring(0,bookChapterJson.getString("cataloguename").indexOf("章")).replace("第", "");
					if(NumberUtil.isNumeric(bookchapterNumber)){						
					bookChapter.setNumber(new Integer(bookchapterNumber));
					}else{
						bookChapter.setNumber(NumberUtil.chineseToNumber(bookchapterNumber));
					}
					//标题
					bookChapter.setTitle(bookChapterJson.getString("cataloguename").substring(bookChapterJson.getString("cataloguename").indexOf("章")+1));
					//内容
					bookChapter.setContent(bookChapterJson.getString("content").replace(".", ".\n\n").replace("。", "。\n\n"));
					bookChapterList.add(bookChapter);
					bookChapter.setBookId(book.getBookId());
					bookChapterService.addBookChapter(bookChapter);
				}
				book.setBookChapterList(bookChapterList);
				
			}
			return bookList;
	 }
	 /**
	  * 悦诚书吧抓取
	 * @throws Exception 
	  */
	    //@Scheduled(cron="0/5 * * * * ?")
	    @Scheduled(cron="0 0 4 * * ?")
	    public void cronYueChengShuBaJob() throws Exception{
	    	Integer[] booIdArray = getYueChengShuBaBookIdArray();
			for (int i = 0; i < booIdArray.length; i++) {
			//System.out.println(getYueChengShuBaBook(booIdArray[i]));
				getYueChengShuBaBook(booIdArray[i]);
			}
	        //System.out.println(new Date().toLocaleString()+" >>cron执行....");
	    }
	    /**
	     * 任务完后执行
	     * @throws InterruptedException
	     */
	    //@Scheduled(fixedDelay=ONE_Minute)
	    public void fixedDelayJob() throws InterruptedException{
	    	
	    	System.out.println(new Date().toLocaleString()+" >>fixedDelay执行....");
	    }
	    /**
	     * 没有延时
	     * @throws InterruptedException
	     */
	   // @Scheduled(fixedRate=ONE_Minute)
	    public void fixedRateJob() throws InterruptedException{
	    	Thread.sleep(1000);
	        System.out.println(new Date().toLocaleString()+" >>fixedRate执行....");
	    }

	   // @Scheduled(cron="0/1 * * * * ?")
	    public void cronJob() throws InterruptedException{
	    	
	        System.out.println(new Date().toLocaleString()+" >>cron执行....");
	    }
	    public static void main(String[] args) throws Exception {
			BookStoreJob bsj=new BookStoreJob();
	    	System.out.println(bsj.getYueChengShuBaBookOrderSign());
			String s = bsj.getYueChengShuBaBookOrderList();
			Integer[] booIdArray = bsj.getYueChengShuBaBookIdArray();
			System.out.println(bsj.getYueChengShuBaBookSign(19));
			for (int i = 0; i < booIdArray.length; i++) {
			System.out.println(bsj.getYueChengShuBaBook(booIdArray[i]));
			}
		}
}