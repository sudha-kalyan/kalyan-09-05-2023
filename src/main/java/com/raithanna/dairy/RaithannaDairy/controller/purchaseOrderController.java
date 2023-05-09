package com.raithanna.dairy.RaithannaDairy.controller;
import com.raithanna.dairy.RaithannaDairy.ServiceImpl.Utility.DownloadCsvReport;
import com.raithanna.dairy.RaithannaDairy.models.*;
import com.raithanna.dairy.RaithannaDairy.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.util.*;

@Controller
public class purchaseOrderController {
    @Autowired
    private PurchaseOrderRepository purchaseOrderRepository;

    @Autowired
    private SupplierRepository supplierRepository;
    @Autowired
    private BankRepository bankRepository;
    @Autowired
    private VehicleRepository vehicleRepository;

    @GetMapping("/purchase")
    public String purchaseOrderForm(Model model, HttpSession session) {
        if (session.getAttribute("loggedIn").equals("yes")) {
            List<supplier> Suppliers = supplierRepository.findByOrderByIdDesc();
            List<purchaseOrder> Amts = purchaseOrderRepository.findByOrderByAmtDesc();
            List<bank> bank = bankRepository.findByOrderByIdDesc();
            List<vehicle> vehicle =vehicleRepository.findByOrderByIdDesc();
           purchaseOrder purchaseOrder = purchaseOrderRepository.findTopByOrderByOrderNoDesc();
            Integer orderNo;
            if (purchaseOrder==null) {
                orderNo = 1;
            } else {
                orderNo = purchaseOrder.getOrderNo() + 1;
            }
//            Iterable<purchaseOrder>  purchaseorder = purchaseOrderRepository.findAll();
//            List<Integer> orderNos = new ArrayList<>();
//            for (purchaseOrder i : purchaseorder) {
//                orderNos.add(i.getOrderNo());
//            }
//            System.out.println(purchaseorder);
//            Integer orderNo = 1;
//            if (!orderNos.isEmpty()) {
//                orderNo = Integer.parseInt(String.valueOf(orderNos.get(orderNos.size() - 1))) + 1;
//            }
            System.out.println(Suppliers.size());
            purchaseOrder po = new purchaseOrder();
            model.addAttribute("purchase", po);
            model.addAttribute("supplier", Suppliers);
            model.addAttribute("amt", Amts);
            model.addAttribute("bank", bank);
            model.addAttribute("vehicle", vehicle);
            model.addAttribute("orderNo", orderNo);
            return "purchase";
        }
        List messages = new ArrayList<>();
        messages.add("Login First");
        model.addAttribute("messages", messages);
        return "redirect:/loginPage";
    }


    @GetMapping("/purchaseExcelData")
    public String purchaseExcelOrderForm(Model model, HttpSession session) {
        if (session.getAttribute("loggedIn").equals("yes")) {
            List<supplier> Suppliers = supplierRepository.findByOrderByIdDesc();

            purchaseOrder po = new purchaseOrder();
            model.addAttribute("purchase", po);
            model.addAttribute("supplier", Suppliers);


            return "purchaseExcel";
        }
        List messages = new ArrayList<>();
        messages.add("Login First");
        model.addAttribute("messages", messages);
        return "redirect:/loginPage";
    }


    @GetMapping("/getPurchase")
    public String getPurchase(@RequestParam(name = "orderNo", defaultValue = "1") Integer orderNo, Model model) {
        List<purchaseOrder> list = purchaseOrderRepository.findByOrderNo(orderNo);
        model.addAttribute("list", list);
        return "purchaseDisplay";
    }

    @PostMapping("/purchaseOrderNew")
    public ModelAndView purchaseOrderForm(Model model,@RequestBody List<purchaseOrder> purchaseList, HttpSession session) {
        String invNo ="";
        if (session.getAttribute("loggedIn").equals("yes")) {
            // List<purchaseOrder> list1 = purchaseOrderRepository.findBySupplierAndInvDate(purchaseList.get(0).getSupplier(),purchaseList.get(0).getInvDate());
            List<purchaseOrder> list1 = purchaseOrderRepository.findByInvDate(purchaseList.get(0).getInvDate());

            Set<String> supplierName = new HashSet<>();

            for (purchaseOrder str : list1) {
                System.out.println("set --"+str.getCode()+"-"+str.getInvDate());
                supplierName.add(str.getInvNo());
            }


            System.out.println(list1.size());
            System.out.println(supplierName.size());
            String format = String.format("%03d", supplierName.size() + 1);

            invNo =  purchaseList.get(0).getInvDate() + "-" + purchaseList.get(0).getCode() + "-" + format + "/";


            int subOrder = 0;
            for (purchaseOrder list : purchaseList) {
                subOrder = subOrder+1;
                purchaseOrder po = new purchaseOrder();

                // sno from ui
                po.setSlNo(list.getSlNo());
                po.setInvDate(list.getInvDate());
                po.setSupplier(list.getSupplier());
                po.setVehNumber(list.getVehNumber());
                po.setMilkType(list.getMilkType());
                po.setQuantity(list.getQuantity());
                po.setFatP(list.getFatP());
                po.setSnfP(list.getSnfP());
                po.setTsRate(list.getTsRate());
                po.setLtrRate(list.getLtrRate());
                po.setAmt(list.getAmt());
                po.setPaymentStatus(list.getPaymentStatus());
                po.setBankIfsc(list.getBankIfsc());
                po.setRecDate(list.getRecDate());
                po.setCode(list.getCode());
                po.setInvNo(invNo+subOrder);
                po.setOrderNo(list.getOrderNo());
                po.setSuplCode(list.getSuplCode());

                purchaseOrderRepository.save(po);
            }

            return new ModelAndView("/loginPage");
        }
        List messages = new ArrayList<>();
        messages.add("Login First");
        model.addAttribute("messages", messages);
        return new ModelAndView("/loginPage");
    }

    @PostMapping("/purchaseExcelData")
    public String purchaseExcelData(@RequestParam Map<String, String> body, Model model, HttpServletResponse response, HttpServletRequest request) {
        System.out.println(body);
        purchaseOrder po = new purchaseOrder();
        System.out.println("SupplierCode:" + body.get("code"));
        po.setSupplier(body.get("supplierName"));
        po.setInvDate(String.valueOf(LocalDate.parse(body.get("invDate"))));
        po.setRecDate(String.valueOf(LocalDate.parse(body.get("recDate"))));

        // excel data list
        List<purchaseOrder> list = purchaseOrderRepository.findBySupplierAndInvDateBetween(po.getSupplier(), po.getInvDate(), po.getRecDate());

        String header[] = {"invDate","supplier","vehicleNo","quantity","fatP","snfP","tsRate","milkType","bankName","paymentStatus","vehicleNo"};

        DownloadCsvReport.getCsvReportDownload(response, header, list, "invoice_data.csv");

        System.out.println("Excel Size -- "+list.size());
        return "redirect:/purchaseExcelData";
    }
    @GetMapping("/purchaseViewData")
    public String purchaseViewOrderForm(Model model, HttpSession session) {
        if (session.getAttribute("loggedIn").equals("yes")) {
            List<supplier> Suppliers = supplierRepository.findByOrderByIdDesc();

            purchaseOrder po = new purchaseOrder();
            model.addAttribute("purchase", po);
            model.addAttribute("supplier", Suppliers);
            return "purchaseView";
        }
        List messages = new ArrayList<>();
        messages.add("Login First");
        model.addAttribute("messages", messages);
        return "redirect:/loginPage";
    }

    @PostMapping("/purchaseViewData")
    public String purchaseOrder(@RequestParam Map<String, String> body, Model model, HttpServletResponse response, HttpServletRequest request) {
        System.out.println(body);
        purchaseOrder po = new purchaseOrder();
        System.out.println("SupplierCode:" + body.get("code"));
        po.setSupplier(body.get("supplierName"));
        po.setInvDate(String.valueOf(LocalDate.parse(body.get("invDate"))));
        po.setRecDate(String.valueOf(LocalDate.parse(body.get("recDate"))));
        // excel data list
        List<purchaseOrder> list = purchaseOrderRepository.findBySupplierAndInvDateBetween(po.getSupplier(), po.getInvDate(), po.getRecDate());
        List<supplier> Suppliers = supplierRepository.findByOrderByIdDesc();
        model.addAttribute("supplier", Suppliers);
        model.addAttribute("list", list);
        System.out.println("Excel Size -- " + list.size());
        return "/purchaseView";
    }
    @GetMapping("/purchasePdfData")
    public String purchasePdfForm(Model model, HttpSession session) {
        if (session.getAttribute("loggedIn").equals("yes")) {
            List<supplier> Suppliers = supplierRepository.findByOrderByIdDesc();

            purchaseOrder po = new purchaseOrder();
            model.addAttribute("purchase", po);
            model.addAttribute("supplier", Suppliers);


            return "purchasePdf";
        }
        List messages = new ArrayList<>();
        messages.add("Login First");
        model.addAttribute("messages", messages);
        return "redirect:/loginPage";
    }
    @PostMapping("/purchasePdfData")
    public String purchasePdfData(@RequestParam Map<String, String> body, Model model, HttpServletResponse response, HttpServletRequest request) {
        System.out.println(body);
        purchaseOrder po = new purchaseOrder();
        System.out.println("invNo:" + body.get("No"));
        po.setSupplier(body.get("supplierName"));
        po.setInvDate(String.valueOf(LocalDate.parse(body.get("invDate"))));
        // po.setSlNo(body.get("slNo"));
        // po.setSnfP(body.get("snfP"));

        // excel data list
        List<purchaseOrder> list = purchaseOrderRepository.findBySupplierAndInvDateBetween(po.getSupplier(),po.getInvDate(),po.getRecDate());

        String header[] = {"invDate","supplier","vehicleNo","quantity","fatP","snfP","tsRate","milkType","bankName","paymentStatus","vehicleNo"};

        DownloadCsvReport.getCsvReportDownload(response, header, list, "invoice_data.csv");

        System.out.println("Excel Size -- "+list.size());
        return "redirect:/purchaseExcelData";
    }


}
