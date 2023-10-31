package servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/books")
public class Books extends HttpServlet {
    private List<Book> bookList = new ArrayList<>();

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        out.println("<html>");
        out.println("<head><title>Book List</title></head>");
        out.println("<body>");
        out.println("<h1>Book List</h1>");

        // Display the list of books
        out.println("<ul>");
        for (Book book : bookList) {
            out.println("<li>" + book.getTitle() + " (ISBN: " + book.getIsbn() + ", Price: $" + book.getPrice() + ") ");
            out.println("<form method='post' action='books'>");
            out.println("<input type='hidden' name='isbn' value='" + book.getIsbn() + "'>");
            out.println("<input type='submit' value='Delete'></form></li>");
        }
        out.println("</ul>");

        // Form to add a new book
        out.println("<h2>Add a New Book</h2>");
        out.println("<form method='post' action='books'>");
        out.println("ISBN: <input type='text' name='isbn'><br>");
        out.println("Title: <input type='text' name='title'><br>");
        out.println("Price: $<input type='text' name='price'><br>");
        out.println("<input type='submit' value='Add Book'>");
        out.println("</form>");

        out.println("</body>");
        out.println("</html>");
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String isbn = request.getParameter("isbn");
        String title = request.getParameter("title");
        String priceStr = request.getParameter("price");

        if (isbn != null && title != null && priceStr != null) {
            try {
                double price = Double.parseDouble(priceStr);
                Book newBook = new Book(isbn, title, price);
                bookList.add(newBook);
            } catch (NumberFormatException e) {
                throw new ServletException("Invalid price", e);
            }
        } else {
            // Handle book deletion request
            String isbnToDelete = request.getParameter("isbn");
            if (isbnToDelete != null) {
                for (Book book : bookList) {
                    if (book.getIsbn().equals(isbnToDelete)) {
                        bookList.remove(book);
                        break;
                    }
                }
            }
        }

        // Redirect back to the book list page
        response.sendRedirect(request.getContextPath() + "/books");
    }
}
