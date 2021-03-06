#   掘金专栏  https://juejin.im/post/5bab32c35188255c8d0fd0a8

# 项目使用kotlin 和java 混合开发,业务部分是kotlin开发,框架基类部分是java开发

# 技术热点

1. Dagger 模式解耦 
2. Retrofit Rxjava RxAndroid    网络框架还管理以及响应式编程类库
3. Butterknife  (也可以使用kotlin  和databing 替换butterknife使用)
4. fragmention  fragment管理以及实现侧滑功能
5. rxpermissions 权限管理
6.  Skin 插件  鸿洋大神的换肤类库,自己再其基础上扩展了一些控件的兼容
7. 日志拦截可视化显示
8. 使用apt编译时生成代码  减少重复代码
9. svgplayer 动画
10. AutoDispose 和 lifecycle 对订阅生命周期进行管理
11. tinker 热更新
12. 百度人脸检测
13. Lottie 动画
14. 今日头条适配
15. aspectj 切面打点日志

## 使用方式

1. 依赖mvp核心类库

   ```
   //日志拦截库  是基于okhttp的拦截机制
   //1.调用 SuspensionView.getInstance().init(this)开启拦截功能,
   //2.需要给okhttp传入网络拦截builder.addNetworkInterceptor(newOkNetworkMonitorInterceptor());
   
   debugApi 'com.lz:intercept_debug:1.0.2'
   releaseApi 'com.lz:intercept_release:1.0.0'
   
   //封装了mvp的基础代码
   api 'com.lz:fram_lib:1.0.1'
   
   //注解库 ,用于减少dagger库连接acticity/fragment 的inject方式,
   需要在使用到dagger的类上使用InjectActivity/InjectFragment/InjectUtis 来注解当前class 
   会在编译时自动生成关联代码
   //注解库
   api 'com.lz:inject_annotations:1.0.4'
   //apt注解工具
   kapt 'com.lz:inject_compile:1.0.4'
   
   //fragment管理  和侧滑退出类库
   api 'me.yokeyword:fragmentation:1.3.5'
   api 'me.yokeyword:fragmentation-swipeback:1.3.6'
   
   //其他功能类库可自行添加
   
   
   ```

1. 配置框架全局参数  GlobalConfiguration 

   ```
   //在manifest里配置 全局参数配置class 路径   参数设置参照 GlobalConfiguration 这个类
   <!-- 网络框架配置 -->
   <meta-data
       android:name="com.lz.framecase.logic.GlobalConfiguration"
       android:value="ConfigModule" />
   ```
1. 创建Component 连接@module 注解类提供的资源对象
   ```
    @CustomizeScope
    @Component(dependencies = AppComponent.class)
    public interface FragmentComponent {
         void inject(ImagePagerFragment fragment);
     }

    ```
   

1. 继承BaseActivity/BaseFragment  基类  重写两个方法
   这里用到了两个注解  @InjectActivity  和  @AttachView@InjectActivity这个是注册当前class到component中的 , @AttachView 这个是管理presenter的回调周期 以及释放资源的 .  其他Fragment 以及其他class的使用方式参照项目中的案例

   ```
   //Dagger 关联activity的注解
   @InjectActivity
   public class MainActivity2 extends BaseActivity {
   
       @Inject   //dagger注解
       @AttachView  // 为presenter 注入页面view的注解
       Main2Presenter mPresenter;
   
       @Override
       protected int getLayout() {
       //布局文件设置
           return R.layout.activity_main2;
       }
   
   
       @Override
       protected void init() {
       //初始化操作
           Button button = (Button) findViewById(R.id.button);
           button.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   mPresenter.getNewLists();
               }
           });
       }
   }
   ```
   


