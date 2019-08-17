package in.co.sdrc.cap.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.util.ResourceUtils;

import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;

public class HeaderFooter extends PdfPageEventHelper{
	
	/** Alternating phrase for the header. */
	Phrase[] header = new Phrase[2];
	
	/** Current page number (will be reset for every chapter). */
	int pagenumber;
	private String domainName;

	public HeaderFooter(String domainName) {
		this.domainName=domainName;
	}

	/**
	 * Initialize one of the headers.
	 * 
	 * @see com.itextpdf.text.pdf.PdfPageEventHelper#onOpenDocument(com.itextpdf.text.pdf.PdfWriter,
	 *      com.itextpdf.text.Document)
	 */
	public void onOpenDocument(PdfWriter writer, Document document) {
		header[0] = new Phrase("CAP");
	}

	/**
	 * Initialize one of the headers, based on the chapter title; reset the page
	 * number.
	 * 
	 * @see com.itextpdf.text.pdf.PdfPageEventHelper#onChapter(com.itextpdf.text.pdf.PdfWriter,
	 *      com.itextpdf.text.Document, float, com.itextpdf.text.Paragraph)
	 */
	public void onChapter(PdfWriter writer, Document document,
			float paragraphPosition, Paragraph title) {
		header[1] = new Phrase(title.getContent());
		pagenumber = 1;

	}

	/**
	 * Increase the page number.
	 * 
	 * @see com.itextpdf.text.pdf.PdfPageEventHelper#onStartPage(com.itextpdf.text.pdf.PdfWriter,
	 *      com.itextpdf.text.Document)
	 */
	public void onStartPage(PdfWriter writer, Document document) {
		pagenumber++;
		Image image = null;
		try
		{
			image = Image.getInstance(ResourceUtils.getFile("classpath:images/cap-header.jpg").getAbsolutePath());
	
			int indentation = 0;
			float scaler = ((document.getPageSize().getWidth() - indentation) / image.getWidth()) * 100;
			image.scalePercent(scaler);
			image.setAbsolutePosition(0, document.getPageSize().getHeight()	+ document.topMargin() - image.getHeight()+16);
			document.add(image);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
	}

	/**
	 * Adds the header and the footer.
	 * 
	 * @see com.itextpdf.text.pdf.PdfPageEventHelper#onEndPage(com.itextpdf.text.pdf.PdfWriter,
	 *      com.itextpdf.text.Document)
	 */
	public void onEndPage(PdfWriter writer, Document document) {

		Image image;
		Font fontStyle=new Font();
		fontStyle.setColor(255,255,255);
		fontStyle.setSize(8);
		try {
			image = Image.getInstance(ResourceUtils.getFile("classpath:images/cap-footer.jpg").getAbsolutePath());
			int indentation = 0;
			float scaler = ((document.getPageSize().getWidth() - indentation) / image
					.getWidth()) * 100;
			image.scalePercent(scaler);
			image.setAbsolutePosition(0, 0);
			document.add(image);
		} catch (Exception e) {
			e.printStackTrace();
		}
		String date = new SimpleDateFormat("dd-MM-yyyy hh:mm a").format(new Date());
		ColumnText.showTextAligned(writer.getDirectContent(),
				Element.ALIGN_CENTER,
				new Phrase(String.format("Page - %d, Printed on : %s %s",pagenumber,date,domainName),fontStyle),
				(document.getPageSize().getWidth()) / 2,
				document.bottomMargin() - 25, 0);
	}
}