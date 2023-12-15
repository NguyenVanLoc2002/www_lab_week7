# www_lab_week7(Spring Framework)
I.Phần thiết kế
  1. Quản lí thư mục
  - Chia ra làm 2 phần:
    + Backend tạo các thư mục con:
      dto
      enums
      models: lớp chứa thông tin đối tượng 
      pks
      reponsitory: chứa đầy đủ các phương thức xử lý dữ liệu chung nhất
      services: thực hiện các công việc truy vấn phức tạp được sử dụng ở Controller 
    + Frontend chứa các lớp Controller
  2.  Thiết kế mối quan hệ giữa các đối tượng
    a) Các lớp 
  - employee (emp_id, full_name, dob, email, phone, address, status)
  - product (product_id, name, description, unit, manufacturer_name, status)
  - customer (cust_id, cust_name, email, phone, address)
  - product_image (product_id, image_id, path, alternative)
  - order (order_id, order_date, emp_id, cust_id)
  - order_detail (order_id, product_id, quantity, price, note)
  - product_price (product_id, price_date_time, price, note

    b) Mối quan hệ
  - Một product có nhiều image, một image thuộc về một product. Status chỉ trạng thái kinh doanh 
  của sản phẩm: 1- đang kinh doanh, 0 - tạm ngưng, -1 - không kinh doanh nữa(lớp Enum).
  - Employee có status: 1- đang làm việc, 0 - tạm nghỉ, -1 – nghỉ việc(lớp Enum).
  - Một order có nhiều order_detail, một order_detail thuộc về một order.
  - Một order thuộc về một employee, một employeecó nhiều order.
  - Một customer có nhiều order, một order chỉ thuộc một customer.
  - Một product_detail có một giá (price) được lưu trong product_price. Một giá được xác định 
  bằng product_id và price_date_time. Tại thời điểm lập order, giá được lấy với price_date_time 
  gần nhất.
  --> Các mối quan hệ 1-n sử dụng anotaion @OneToMany và @ManyToOne để liên kết giữa các bảng
    VD: 1 product có nhiều product_image và 1 product_image chỉ thuộc về 1 product
    Ở Lớp Product:
      @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
        private List<ProductImage> productImageList = new ArrayList<>();
    Ở lớp ProductImage:
      @ManyToOne
      @JoinColumn(name = "product_id")
      private Product product;

II. Hiển thị các sản phẩm
  1. Tạo 2 dto ProductInfo và CartItem
Trong ProductInfo lưu trữ productId, imagePath,name và price của sản phẩm.
Trong CartItem lưu trữ ProductInfo và quantity

  2. Hiển thị giao diện shopping thì:
* Trong ShoppingController:
 - Tạo 1 đường dẫn /shopping(http://localhost:8080/shopping) để chuyển đến trang html user/shopping
 - Lấy danh sách Product Info bằng việc viết truy vấn Join 3 bảng product, product_image và product_price ở trong lớp ProductRepository lấy giá của sản phẩm có giá gần với thời điểm hiện tại nhất.
-->    @Query("SELECT DISTINCT NEW vn.edu.iuh.fit.www_lab_week7.backend.dto.ProductInfo(p.product_id,pi.path, p.name, pp.price) " +
            "FROM ProductImage pi " +
            "JOIN pi.product p " +
            "JOIN p.productPrices pp " +
            "WHERE pp.price_date_time = (" +
            "    SELECT MAX(pp2.price_date_time) " +
            "    FROM ProductPrice pp2 " +
            "    WHERE pp2.price_date_time <= :cur AND pp2.product.product_id = p.product_id)")
    List<ProductInfo> findAllProductInfoWithLatestPrice(@Param("cur") LocalDateTime cur);
 - Sau khi lấy danh sách do mỗi sản phẩm có nhiều ảnh nên xử lí việc trùng Id của ProductInfo bằng stream và toMap --> List<ProductInfo> productInfos = productInfoList.stream().collect(Collectors.toMap(ProductInfo::getProductId, Function.identity(), (existing, replacement) -> existing)).values().stream().collect(Collectors.toList());
 - Tạo 1 cartItem mới
 - Sau đó add cartItem và listProductInfo vào model để xử lý trong trang user/shopping.html

* Giao diện shopping:
- Dùng bootstrap để tạo giao diện với mẫu sẵn 
- Dùng 1 thẻ div với th:each để render ra từng thông tin của ProductInfo trong listProductInfo.
  
III.Chọn vào giỏ hàng và thanh toán
  1. Chọn vào giỏ hàng
   Ở giao diện shopping.html:
- Tạo 1 form chuyển đến /addToCart với phương thức get để lấy các thông tin của productInfo, chuyển các thông tin này sang /addToCart sau khi nhấn vào button Add to cart
- Ở số lượng giỏ hàng sẽ kiểm tra session của cartItemList có null không nếu có thì set số lượng bằng 0, ngược lại lấy session.cartItemList.size().

  Ở ShoppingController:
- Dùng HttpSession để lưu trữ session chứa listCartItem, dùng @RequestParam để lấy thông tin của ProductInfo từ trang shopping.html
- Tạo 1 List<CartItem> list = (List<CartItem>) session.getAttribute("cartItemList") 
 + Nếu session null thì tạo 1 mảng mới
 + Ngược lại:
    Tạo 1 biến found = false; đặt làm cờ
    Dùng vòng lặp for:each để kiểm tra xem sản phẩm có bị trùng không nếu có thì tăng số lượng lên 1 và found = true.
    Kiểm tra biến found nếu là false thì Tạo 1 CartItem mới với số lượng mặc định bằng 1 và thêm CartItem vào list (List<CartItem>)
- session.setAttribute("cartItemList", list);
- Chuyển hướng đến /shopping
  
 Ở giao diện shopping.html:
- Sau khi ấn nút thì sản phẩm sẽ được thêm vào giỏ hàng(session.cartItemList) và set lại số lượng các sản phẩm được thêm vào giỏ hàng (số lượng này chỉ thay đổi đối với các sản phẩm khác nhau được thêm vào)

    2. Thanh toán
- Khi nhấn vào biểu tượng giỏ hàng ở giao diện giỏ hàng thì nó sẽ chuyển đến /cart trong ShoppingController
- Ở ShoppingController:
  + Lấy danh sách các CartItem trong session List<CartItem> list = (List<CartItem>) session.getAttribute("cartItemList");
  + Tính tổng tiền = price * quantity theo từng sản phẩm 
  + Thêm vào model tổng tiền và danh sách các CartItem trong session
- Chuyển đến giao diện user/checkout.html
- Ở giao diện checkout.html hiển thị các sản phẩm được thêm vào cùng với số lượng(có thể tăng giảm ở đây) của sản phẩm, cũng như tổng tiền từ danh sách các CartItem trong session và totalPrice được thêm trong model.

- Ở phần thay đổi số lượng của sản phẩm tạo 1 form chuyển đến /updateQuantity với method = post:
  + Tạo 1 input ẩn lưu trữ id của product
  + 1 Input lấy số lượng của product
- Ở  @PostMapping("/updateQuantity") lấy dữ liệu id và quantity bằng @RequestParam từ giao diện sau đó:
  + Lấy danh sách cartItem trong session
  + Dùng for:each kiểm tra trùng id nếu trùng thì set lại quantity của cartitem
  + Chuyển hướng đến /cart
 
- Khi ấn nút Go to checkout ở giao diện thì chuyển đến  @GetMapping("/checkout") trong ShoppingController , sau đó set session rỗng bằng session.invalidate(), 
chuyển hướng về lại trang Shopping (do em không làm phần cụ thể thanh toán).


IV. Trang Admin(http://localhost:8080/)
1. CRUD product
  * Read List Product
- Ở giao diện chính khi ấn <a th:href="@{/products}">Danh sách sản phẩm</a><br> thì sẽ qua ProductController 
- Trong phương thức @GetMapping("/products"):
  + Xử lí phân trang: 
   Page<Product> productPage = productService.findPagingnated(currentPage-1, pageSize, "name", "asc");
   mặc định mỗi trang hiển thị 10 sản phẩm sắp xếp theo "name" từ a-z.
   Thêm productPage vào model 
  + Lấy số lượng trang theo tổng số sản phẩm/ số phẩm trên mỗi trang, rồi thêm vào model
  + Chuyển đến trang listProduct.html

- Ở giao diện listProduct.html:
  + Tạo 1 thẻ div với th:switch="${productPage}" để kiểm tra:
    Nếu null thì hiển thị "Not products yet"
    Ngược lại hiển thị Bảng lưu trữ thông tin của từng sản phẩm và dòng phân trang.

  * Create Product
- Ở giao diện listProduct.html khi click vào  <a class="btn btn-dark" th:href="@{/addProduct}">Insert</a> thì chuyển đến ProductController
- Tại phương thức @GetMapping("/addProduct"):
  + Tạo mới 1 product và ProductStatus rồi thêm vào model
  + Chuyển đến giao diện addProduct.html
- Ở giao diện addProduct.html tạo 1 form với action="/addProduct/new" method="post" và th:object="${product} để truyền vào các giá trị của product từ các input:
  + Tạo các input với các thông tin tương ứng với Product 
    Vd:  <input th:id="name" th:field="*{name}"><br><br> --> truyền thuộc tính name vào object product
  + Tạo 1 nút submit để chuyển đến /addProduct/new trong ProductController 
  + Tạo 1 nút reset để set rỗng lại các input
- Sau khi ấn submit thì trong  @PostMapping("/addProduct/new") trong ProductController ta lấy product bằng @ModelAttribute("product") Product product 
  gọi phương thức save trong productService để lưu đối tượng product mới  productService.saveProduct(product);
  --> Chuyển hướng lại đến /products
  
  * Delete Product
 - Ở giao diện listProduct.html khi click vào <a class="btn btn-danger" th:href="@{/delete/{id}(id=${product.product_id})}" >Delete</a> thì chuyển đến ProductController
 - Tại phương thức @GetMapping("/delete/{id}") lấy id product thông qua @PathVariable("id") 
 - Dùng phương thức deleteProductById(id) trong productService để xóa sản phẩm
 - Chuyển hướng lại đến /products

  * Update Product
 - Ở giao diện listProduct.html khi click vào <a class="btn btn-primary" th:href="@{/edit/{id}(id=${product.product_id})}">Edit</a> thì chuyển đến ProductController
 - Tại phương thức  @GetMapping("/edit/{id}") lấy id product thông qua @PathVariable("id"):
   + Tìm sản phẩm theo id
   + Thêm sản phẩm vừa tìm được vào model
   + Chuyển đến giao diện editProduct.html
 - Ở giao diện editProduct.html tạo form action="/editProduct" method="post" và th:object="${product} vừa để render ra thông tin sản phẩm 
    vừa để cập nhật lại giá trị sau khi ấn nút submit --> chuyển đến /editProduct ở ProductController
 - Tại phương thức @PostMapping("/editProduct") lấy product mới edit lại bằng @ModelAttribute("product") Product product:
   + Kiểm tra xem sản phẩm này đã tồn tại chưa:
    Nếu tồn tại(!=null) thì set lại các giá trị của sản phẩm rồi save bằng productService.saveProduct(existingProduct);
   + Chuyển hướng đến /products
  
2. Thống kê order theo ngày, theo khoảng thời gian
 - Ở giao diện chính khi click vào <a th:href="@{/ordersByOrderDate}">Danh sách hóa đơn theo ngày</a><br> thì chuyển đến OrderController
 - Tại phương thức @GetMapping("/ordersByOrderDate") showFormFindOrderByDate bằng việc chuyển đến giao diện listOrderByOrderDate.html
 - Ở giao diện listOrderByOrderDate.html tạo 1 <form action="/orders/date" method="get"> chứa 1 input cho chọn ngày và 1 nút Submit để truyền dữ liệu ngày qua OrderController
 - Tại phương thức  @GetMapping("/orders/date"):
   +  Lấy ngày vừa chọn bằng HttpServletRequest -->  LocalDate orderDate = LocalDate.parse(request.getParameter("orderDate"));
   +  Tìm danh sách order theo ngày bằng câu truy vấn sql trong orderService
   +  Thêm danh sách order vào model --> model.addAttribute("orderList",orderList);
   +  Chuyển đến giao diện listOrderByOrderDate.html
 - Ở giao diện listOrderByOrderDate.html dùng thẻ div để render thông tin danh sách order
      <h2>Danh sách hóa đơn</h2>
      <div th:each="order : ${orderList}">
          Tên nhân viên: <span th:text="${order.getEmployee().fullname}"></span><br>
          Tên khách hàng: <span th:text="${order.getCustomer().name}"></span><br>
          ...
      </div>
3. Thống kê order theo nhân viên bán hàng trong 1 khoảng thời gian.
 - Ở giao diện chính khi click vào <a th:href="@{/ordersByEmployeeBetweenOrderDate}">Danh sách hóa đơn theo nhân viên trong 1 khoảng thời gian</a><br>
 thì chuyển đến OrderController
 - Tại phương thức @GetMapping("/ordersByEmployeeBetweenOrderDate") showFormFindOrderByEmployeeBetweenOrderDate bằng việc chuyển đến giao diện listOrderByEmpBetweenOrderDate.html
 - Ở giao diện listOrderByEmpBetweenOrderDate.html tạo 1 <form action="/orders/emp-and-date" method="get">:
    Tạo 2 input cho chọn ngày bắt đầu và ngày kết thúc và 1 nút Submit để truyền dữ liệu ngày bắt đầu và ngày kết thúc qua OrderController
 - Tại phương thức @GetMapping("/orders/emp-and-date"):
   + Lấy ngày bắt đầu và ngày kết thúc bằng HttpServletRequest:
     LocalDate startDate = LocalDate.parse(request.getParameter("startDate"));
     LocalDate endDate = LocalDate.parse(request.getParameter("endDate"));
   + Lấy danh sách nhân viên bằng câu truy vấn sql tìm nhân viên theo ngày bắt đầu và ngày kết thúc đã viết ở orderService bằng việc join 2 bảng order và employee có empId bằng nhau 
   + Thêm vào model danh sách nhân viên
   + Thêm vào session ngày ngày bắt đầu và ngày kết thúc để có thể lấy ngày đó hiển thị ở giao diện danh sách hóa đơn trong khoảng thời gian theo nhân viên
   + Chuyển đến giao diện listOrderByEmpBetweenOrderDate.html
 - Ở giao diện listOrderByEmpBetweenOrderDate.html dùng <div th:switch="${employees}"> để kiểm tra:
   + Nếu null thì hiển thị Not emplyee yet
   + Ngược lại render ra thông tin nhân viên theo dạng bảng và mỗi nhân viên có nút view để xem danh sách các hóa đơn của người nó theo khoảng thời gian vừa chọn.
 - Khi ấn vào nút <a class="btn btn-primary" th:href="@{/orders/emp-by-date/{id}(id=${emp.id})}">View</a> thì nó chuyển đến OrderController
 - Tại phương thức  @GetMapping("/orders/emp-by-date/{id}") lấy id nhân viên bằng @PathVariable("id") Long id và dùng HttpSession để lấy ngày bắt đầu và ngày kết thúc:
   + Lấy danh sách order theo Id và khoảng thời gian đã chọn bằng câu lệnh sql trong OrderService cụ thể là tìm trong bảng order --> order nào có empId =id và orderDate nằm 
      giữa startDate và endDate thì lấy.
   + Chuyển đến trang listDetailOrderByEmpBetweenOrderDate.html
 - Ở giao diện listOrderByEmpBetweenOrderDate.html dùng thẻ   <div th:each="od : ${orderList}"> để render ra thông tin từng hóa đơn phù hợp:
     <h2>Danh sách hóa đơn theo nhân viên trong 1 khoảng thời gian</h2>
    <div th:each="od : ${orderList}">
        Tên nhân viên: <span th:text="${od.getEmployee().fullname}"></span><br>
        ....
    </div>



  

