package com.safly.web.servelt;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;

import com.safly.domain.Category;
import com.safly.domain.Product;
import com.safly.service.AdminService;
import com.safly.service.impl.AdminServiceImpl;
import com.safly.utils.CommonUtils;

/**
 * Servlet implementation class AdminAddProductServlet
 */
public class AdminAddProductServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AdminAddProductServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		Product product = new Product();
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			DiskFileItemFactory factory = new DiskFileItemFactory();
			ServletFileUpload upload = new ServletFileUpload(factory);
			List<FileItem> parseRequest = upload.parseRequest(request);
			for (FileItem item : parseRequest) {
				boolean formFiled = item.isFormField();
				if (formFiled) {
					String fieldName = item.getFieldName();
					String fieldValue = item.getString("UTF-8");
					
					System.out.println("fieldName--"+fieldName+",fieldValue--"+fieldValue);
					map.put(fieldName, fieldValue);
					
				}else{
						String name = item.getName();
						String path = this.getServletContext().getRealPath("upload");
						InputStream inputStream = item.getInputStream();
						OutputStream sOutputStream = new FileOutputStream(path+"/"+name);
						IOUtils.copy(inputStream, sOutputStream);
						inputStream.close();
						sOutputStream.close();
						item.delete();
						
						map.put("pimage", "upload/"+name);
				}
			}
			
			BeanUtils.populate(product, map);
			product.setPid(CommonUtils.getUUid());
			product.setPdate(new Date());
			product.setPflag(0);
			Category category = new Category();
			category.setCid(map.get("cid").toString());
			product.setCategory(category);
			
			AdminService service = new AdminServiceImpl();
			service.saveProduct(product);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
