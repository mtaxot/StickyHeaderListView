# StickyHeaderListView
StickyHeaderListView is also known as PinedHeaderListView, this is a simple but powerful implementation.

其实写这个东西不是什么项目需要，之前在网上看到过有一个人实现过这个东西，是黑色的背景的那个， 当时我还以为是用了WMS创建的悬浮窗口呢，觉得很神秘，于是下载下来代码看了看，发现代码很难被复用，别的不说，这么一个简单的功能用不了多少代码啊，后来我就不看那个实现了。
具体可以参考这个：https://blog.csdn.net/xiechengfa/article/details/39005775
然后又看到了另一个人的实现，这个看着比较干净，代码也比较有组织， 叫PinnedSectionListView
具体可以参考这个：https://blog.csdn.net/jdsjlzx/article/details/20697257

我的这个实现原理也很简单：
基本思想是 为了创建一个浮动的Title，我采用一个继承自FrameLayout的Wrapper，当listview滚动到时候，判断是否应该在这个Wrapper内固定一个Title出来。
如果在这个wrapper事先固定好一个Title那就太low了，索性我干脆根据listview的item 对应的view来创建，这样就可以支持任意布局的title


具体详见注释
