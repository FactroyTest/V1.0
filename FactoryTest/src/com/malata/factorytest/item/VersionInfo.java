package com.malata.factorytest.item;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.w3c.dom.Text;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.os.SystemProperties;

import com.malata.factorytest.R;

public class VersionInfo extends AbsHardware {
	private TextView IMEI1;// 设备串号
	private TextView IMEI2;// 串号2
	private TextView SW;
	private TextView WIFIMAC;
	private TextView BTADDR;
	private TextView android_os;
	private TextView textview_devicemodel;
	private TextView textview_wifiip;
	private TextView CPU;
	private TextView Board;
	private TextView Brand;
	private TextView Manufacturer;
	private TextView BuildTime;
	private TextView Incremental;
	private TextView hardVersion;
	private TextView hardinfo;
	private TextView phoneSerialTextView,phoneKernal,deviceModem;
	String imei_1;
	String imei_2;
	String phoneSerial;
	
	private static final String KEY_IMEI = "imei";
	private TelephonyManager  telephonyManager;
    private static final String FILENAME_PROC_VERSION = "/proc/version";
	private Context context;
	public VersionInfo(String text, Boolean visible) {
		super(text, visible);
		
	}
	
	@Override
	public View getView(Context context) {
		this.context = context;
		View view = LayoutInflater.from(context).inflate(R.layout.version_battery, null);
		IMEI1 = (TextView) view.findViewById(R.id.imei1);
		IMEI2 = (TextView) view.findViewById(R.id.imei2);
		SW = (TextView) view.findViewById(R.id.sw);
		WIFIMAC = (TextView) view.findViewById(R.id.wifimac);
		BTADDR = (TextView) view.findViewById(R.id.btaddrc);
		android_os =(TextView) view.findViewById(R.id.textview_android_os);
		textview_devicemodel = (TextView) view.findViewById(R.id._textview_devicemodel);
		CPU = (TextView) view.findViewById(R.id.text_CPU);
		Board = (TextView) view.findViewById(R.id.text_board);
		Brand = (TextView) view.findViewById(R.id.text_brand);
		Manufacturer = (TextView) view.findViewById(R.id.text_MANUFACTURER);
		BuildTime = (TextView) view.findViewById(R.id.bulidtime);
		textview_wifiip = (TextView) view.findViewById(R.id.wifiip);
		Incremental = (TextView) view.findViewById(R.id.text_INCREMENTAL);
		hardVersion = (TextView) view.findViewById(R.id.text_firmware);
		hardinfo = (TextView) view.findViewById(R.id.text_hard);
		phoneSerialTextView = (TextView) view.findViewById(R.id.serial_text);
		phoneKernal = (TextView) view.findViewById(R.id.Kernal_text);
		deviceModem = (TextView) view.findViewById(R.id.text_Modem);
		return view;
		
	}
		
	@SuppressWarnings("deprecation")
	@SuppressLint({ "NewApi", "ResourceAsColor", "DefaultLocale" })
	@Override
	public TestResult test() {
		 telephonyManager = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		
		String imei = telephonyManager.getDeviceId();// 设备串号
		String imei2 = telephonyManager.getLine1Number();// 设备串号2
		String IMSI = telephonyManager.getSubscriberId();// sim卡id号
		String deviceModel = android.os.Build.MODEL;//产品Model
		String apiVersion = android.os.Build.VERSION.SDK;//api
		String systemVersion = android.os.Build.VERSION.RELEASE;//Android系统版本
		String sysyemVersioncodename = android.os.Build.VERSION.CODENAME; 
		String systemCPU = android.os.Build.CPU_ABI;//处理器
		String systemBoard = android.os.Build.BOARD; //芯片
		String systemBrand = android.os.Build.BRAND; //品牌
		String systemManufacturer = android.os.Build.MANUFACTURER; //厂商
		String systemsw = android.os.Build.DISPLAY;//ROM版本
		//long systemBulidTime = android.os.Build.TIME;
		String sysIncremental = android.os.Build.VERSION.INCREMENTAL;//基带信息
		String systemhard = android.os.Build.HARDWARE;//硬件信息
		String systemhardID = android.os.Build.ID;
		String systemhardhost = android.os.Build.HOST;
		phoneSerial = android.os.Build.SERIAL;
		
		//IMEI2.setText();
		IMEI1.setText(imei);
		phoneSerialTextView.setText(phoneSerial);
		phoneKernal.setText(getLinuxKernalInfoEx());
		SW.setText(systemsw);
		android_os.setText("SDK"+" "+apiVersion);//
		textview_devicemodel.setText(deviceModel);
		CPU.setText(systemCPU);
		Board.setText(systemBoard);
		Brand.setText(systemBrand);
		Manufacturer.setText(systemManufacturer);
		//BuildTime.setText(Long.toString(systemBulidTime));
		BuildTime.setText(getFormattedKernelVersion());
		
		Incremental.setText(sysIncremental);
		hardinfo.setText(systemhard+","+systemhardhost+","+systemhardID);
		hardVersion.setText(systemVersion+","+sysyemVersioncodename);
		
		//获取蓝牙地址
		BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		String bluetoothAddress = bluetoothAdapter.getAddress();
		BTADDR.setText(bluetoothAddress);
		//获取WiFi地址+IP
		WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		WifiInfo info = wifi.getConnectionInfo();
		WIFIMAC.setText(info.getMacAddress());
		int ipAddress = info.getIpAddress(); 
			String ip = String.format("%d.%d.%d.%d", 
					(ipAddress & 0xff), 
	                (ipAddress >> 8 & 0xff), 
	                (ipAddress >> 16 & 0xff), 
	                (ipAddress >> 24 & 0xff)); 
		textview_wifiip.setText(ip);//IP地址
		
		return getResult();
	}
	
	//获取内核版本
	public String getLinuxKernalInfoEx() {
		String result = "";
		String line;
		String[] cmd = new String[] { "/system/bin/cat", "/proc/version" };
		String workdirectory = "/system/bin/";
		try {
			ProcessBuilder bulider = new ProcessBuilder(cmd);
			bulider.directory(new File(workdirectory));
			bulider.redirectErrorStream(true);
			Process process = bulider.start();
			InputStream in = process.getInputStream();			
			InputStreamReader isrout = new InputStreamReader(in);
			BufferedReader brout = new BufferedReader(isrout, 8 * 1024);

			while ((line = brout.readLine()) != null) {
				result += line;
			}
			in.close();			
		} catch (Exception e) {
			e.printStackTrace();
		}
		//Log.i("内核版本","----Linux Kernal is :"+result);
		return result;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		
		telephonyManager = (TelephonyManager) 
				context.getSystemService(Context.TELEPHONY_SERVICE);
		
		String baseversion = "gsm.version.baseband"; 
		
		//deviceModem.setText(baseversion);
		int modemSlot = getExternalModemSlot();
		 boolean hasExternalModem = modemSlot != 0; 
		 
		 
		 /*if (hasExternalModem && !FeatureOption.PURE_AP_USE_EXTERNAL_MODEM) {
	           if (modemSlot == 1) {
	               baseversion = "gsm.version.baseband.2";
	           }
	        }
	        Log.d("baseversion","baseversion=" + baseversion);
	          //给布局
	        //setValueSummary(KEY_BASEBAND_VERSION, baseversion);

	        if (hasExternalModem && !FeatureOption.PURE_AP_USE_EXTERNAL_MODEM) {
	            String version2 = "gsm.version.baseband.2";
	            if (FeatureOption.EVDO_DT_SUPPORT) {
	                version2 = "cdma.version.baseband";
	            } else {
	                if (modemSlot == 1) {
	                    version2 = "gsm.version.baseband";
	                }
	            }
	            Log.i("version2", "version2=" + version2);
	            //给布局
	            //setValueSummary(KEY_BASEBAND_VERSION_2, version2);
	            updateBasebandTitle();
	        } else {
	        	//取消布局
	            getPreferenceScreen().removePreference(
	                    findPreference(KEY_BASEBAND_VERSION_2));
	        }*/
		 
		
	}
	
	
	    /*M: for support gemini feature and C+D two 
	    *   moderm
	    */
	    
	  /*  private void updateBasebandTitle() {
	        String basebandversion = context.getString(R.string.baseband_version);
	        String slot1;
	        String slot2;
	        if (FeatureOption.EVDO_DT_SUPPORT) {
	            Locale tr = Locale.getDefault();// For chinese there is no space
	            slot1 = "GSM " + basebandversion;
	            slot2 = "CDMA " + basebandversion;
	            if (tr.getCountry().equals(Locale.CHINA.getCountry())
	                    || tr.getCountry().equals(Locale.TAIWAN.getCountry())) {
	                slot1 = slot1.replace("GSM ", "GSM");
	                slot2 = slot2.replace("CDMA ", "CDMA");// delete the space
	            }
	        } else {
	        	//IMEI 插槽1
	            //slot1 = getString(R.string.status_imei_slot1);
	            slot1 = basebandversion
	                    + slot1.replace(getString(R.string.status_imei), " ");
	            //IMEI 插槽2
	            //slot2 = getString(R.string.status_imei_slot2);
	            //status_imei ==  IMEI
	            slot2 = basebandversion
	                    + slot2.replace(getString(R.string.status_imei), " ");
	        }
	       // 给布局
	        findPreference(KEY_BASEBAND_VERSION).setTitle(slot1);
	        findPreference(KEY_BASEBAND_VERSION_2).setTitle(slot2);
	    }*/

	
	

//获得modem信息
    private int getExternalModemSlot() {
        int modemSlot = 0;
        String md = SystemProperties.get("ril.external.md",
                    context.getResources().getString(R.string.device_info_default));
        if (md.equals(context.getResources().getString(R.string.device_info_default)))
        {
           // modemSlot = PhoneConstants.GEMINI_SIM_1;
        } else {
            modemSlot = Integer.valueOf(md).intValue();
        }
        Log.d("mode版本","modemSlot = " + modemSlot);
        return modemSlot;
    }
	
    
    
    
	
	/**
     * Reads a line from the specified file.
     * @param filename the file to read from
     * @return the first line, if any.
     * @throws IOException if the file couldn't be read
     */
    private static String readLine(String filename) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filename), 256);
        try {
            return reader.readLine();
        } finally {
            reader.close();
        }
    }
	 public static String formatKernelVersion(String rawKernelVersion) {
	        // Example (see tests for more):
	        // Linux version 3.0.31-g6fb96c9 (android-build@xxx.xxx.xxx.xxx.com) \
	        //     (gcc version 4.6.x-xxx 20120106 (prerelease) (GCC) ) #1 SMP PREEMPT \
	        //     Thu Jun 28 11:02:39 PDT 2012

	        final String PROC_VERSION_REGEX =
	            "Linux version (\\S+) " + /* group 1: "3.0.31-g6fb96c9" */
	            "\\((\\S+?)\\) " +        /* group 2: "x@y.com" (kernel builder) */
	            "(?:\\(gcc.+? \\)) " +    /* ignore: GCC version information */
	            "(#\\d+) " +              /* group 3: "#1" */
	            "(?:.*?)?" +              /* ignore: optional SMP, PREEMPT, and any CONFIG_FLAGS */
	            "((Sun|Mon|Tue|Wed|Thu|Fri|Sat).+)"; /* group 4: "Thu Jun 28 11:02:39 PDT 2012" */
	        Matcher m = Pattern.compile(PROC_VERSION_REGEX).matcher(rawKernelVersion);
	        if (!m.matches()) {
	            Log.e("...", "Regex did not match on /proc/version: " + rawKernelVersion);
	            return "Unavailable";
	        } else if (m.groupCount() < 4) {
	            Log.e("...", "Regex match on /proc/version only returned " + m.groupCount()
	                    + " groups");
	            return "Unavailable";
	        }
	        return m.group(4) ; // + "\n" +                 // 3.0.31-g6fb96c9  caoqiaofeng del SFZZAH-1 20131205
	           // m.group(2) + " " + m.group(3) + "\n" + // x@y.com #1
	           // m.group(4);                            // Thu Jun 28 11:02:39 PDT 2012
	 }
	 public static String getFormattedKernelVersion() {
	        try {
	            return formatKernelVersion(readLine(FILENAME_PROC_VERSION));

	        } catch (IOException e) {
	            Log.e("...","IO Exception when getting kernel version for Device Info screen",
	                e);
	            return "Unavailable";
	        }
	    }

	 public class PhoneConstants {

		    /** Copied from PhoneApp. See comments in Phone app for more detail. */
		    // TODO: Change phone app to rely on this also.
		    //public static final String EXTRA_CALL_ORIGIN = "com.android.phone.CALL_ORIGIN";

		}

}
