package com.liteway.storecatalog;

import android.app.*;import android.os.*;import android.graphics.*;import android.graphics.drawable.*;import android.view.*;import android.view.inputmethod.EditorInfo;import android.widget.*;import org.json.*;import java.io.*;import java.nio.charset.StandardCharsets;import java.util.*;

public class MainActivity extends Activity {
  ArrayList<JSONObject> items=new ArrayList<>(); LinearLayout result;
  int teal=Color.rgb(15,118,110), navy=Color.rgb(15,23,42);
  public void onCreate(Bundle b){super.onCreate(b); load(); build();}
  TextView tv(String s,int sp,boolean bold){ TextView t=new TextView(this);t.setText(s);t.setTextSize(sp);t.setTextColor(navy);t.setPadding(0,8,0,8);if(bold)t.setTypeface(null,1);return t; }
  void build(){ ScrollView sc=new ScrollView(this); LinearLayout root=new LinearLayout(this);root.setOrientation(LinearLayout.VERTICAL);root.setPadding(28,36,28,36);sc.addView(root); TextView h=tv("Liteway Store Catalog",28,true);h.setTextColor(teal);root.addView(h);root.addView(tv("ابحث بكود الصنف | Search by item code",16,false));
    EditText q=new EditText(this);q.setHint("مثال: RN-04 / SC-262 / FT03");q.setSingleLine(true);q.setImeOptions(EditorInfo.IME_ACTION_SEARCH);q.setTextSize(18);root.addView(q,new LinearLayout.LayoutParams(-1,-2));
    Button btn=new Button(this);btn.setText("بحث / Search");root.addView(btn);result=new LinearLayout(this);result.setOrientation(LinearLayout.VERTICAL);result.setPadding(0,24,0,40);root.addView(result);btn.setOnClickListener(v->search(q.getText().toString()));q.setOnEditorActionListener((v,a,e)->{search(q.getText().toString());return true;});setContentView(sc); }
  void load(){try{InputStream in=getAssets().open("catalog.json");String s=new String(in.readAllBytes(),StandardCharsets.UTF_8);JSONArray a=new JSONArray(s);for(int i=0;i<a.length();i++)items.add(a.getJSONObject(i));}catch(Exception e){}}
  void search(String raw){result.removeAllViews();String q=raw.trim();if(q.isEmpty()){result.addView(tv("اكتب كود الصنف أولاً",16,false));return;} JSONObject hit=null;for(JSONObject o:items)if(o.optString("code").equalsIgnoreCase(q)){hit=o;break;} if(hit==null){result.addView(tv("الكود غير موجود / Code not found",18,true));return;} show(hit);}
  void show(JSONObject o){try{ result.addView(tv(o.getString("code"),26,true)); ImageView im=new ImageView(this);im.setAdjustViewBounds(true);im.setScaleType(ImageView.ScaleType.CENTER_CROP);try{InputStream in=getAssets().open(o.getString("image"));im.setImageBitmap(BitmapFactory.decodeStream(in));}catch(Exception e){} result.addView(im,new LinearLayout.LayoutParams(-1,520));
    result.addView(tv("العربي",15,true));result.addView(tv(o.optString("nameAr"),20,true));String da=o.optString("descriptionAr");if(!da.isEmpty())result.addView(tv(da,16,false));
    result.addView(tv("English",15,true));result.addView(tv(o.optString("nameEn"),20,true));String de=o.optString("descriptionEn");if(!de.isEmpty())result.addView(tv(de,16,false));
    String sp=o.optString("specifications");if(!sp.isEmpty()){result.addView(tv("المواصفات / Specifications",16,true));result.addView(tv(sp,16,false));}
    result.addView(tv("صفحة المصدر / Source page: "+o.optInt("page"),13,false));
  }catch(Exception e){result.addView(tv("تعذر عرض البيانات",16,false));}}
}
