package com.raithanna.dairy.RaithannaDairy.controller;
import com.raithanna.dairy.RaithannaDairy.ServiceImpl.Utility.DownloadCsvReport;
import com.raithanna.dairy.RaithannaDairy.models.bank;
import com.raithanna.dairy.RaithannaDairy.models.purchaseOrder;
import com.raithanna.dairy.RaithannaDairy.models.supplier;
import com.raithanna.dairy.RaithannaDairy.models.vehicle;
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
    private CustomerRepository customerRepository;
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
            System.out.println(Suppliers.size());
            purchaseOrder po = new purchaseOrder();
            model.addAttribute("purchase", po);
            model.addAttribute("supplier", Suppliers);
            model.addAttribute("amt", Amts);
            model.addAttribute("bank", bank);
            model.addAttribute("vehicle", vehicle);
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


//    @PostMapping("/purchase")
//    public String savePurchases(@RequestParam Map<String, String> body, Model model) {
//        //System.out.println("body" + body);
//        purchaseOrder po = new purchaseOrder();
//        System.out.println("SupplierCode:" + body.get("code"));
//        po.setSupplier(body.get("supplierName"));
//        po.setQuantity(Double.parseDouble(body.get("quantity")));
//        // po.setInvDate(LocalDate.parse(body.get("invDate")));
//        po.setFatP(Double.parseDouble(body.get("fatP")));
//        po.setSnfP(Double.parseDouble(body.get("snfP")));
//        po.setTsRate(Double.parseDouble(body.get("tsRate")));
//        po.setMilkType(body.get("milkType"));
//        po.setCode(body.get("supplierName"));
//        po.setPaymentStatus(body.get("paymentStatus"));
//        po.setBankName(body.get("bankName"));
//        double ltrRate;
//        ltrRate = ((Double.parseDouble(body.get("fatP")) + Double.parseDouble(body.get("snfP"))) * Double.parseDouble(body.get("tsRate"))) / 100;
//        po.setLtrRate(ltrRate);
////        //purchaseOrder purchaseOrder = purchaseOrderRepository.findTopByOrderBySlNoDesc();
////        Integer slNo;
////        if (purchaseOrder == null) {
////            slNo = 1;
////        } else {
////            slNo = purchaseOrder.getSlNo() + 1;
////        }
////        po.setSlNo(slNo);
//        Double amt;
//        amt = (Double.parseDouble(body.get("quantity")) * ltrRate);
//        po.setAmt(amt);
//
//
//        // date order
//        List<String> l = new ArrayList<>();
//        l.add(po.getInvDate().toString());
//        //countDistinctRackById(Long id);
//        List<purchaseOrder> ll = purchaseOrderRepository.findByInvDate(po.getInvDate());
//
//        System.out.println("************" + ll.size());
//
//        Set<String> supplierName = new HashSet<>();
//
//        for (purchaseOrder str : ll) {
//            supplierName.add(str.getSupplier());
//        }
//        System.out.println("************ supplierName" + supplierName.size());
//        //List<purchaseOrder> list = purchaseOrderRepository.findBySupplierAndInvDate(po.getSupplier(), po.getInvDate());
//        // main order logic
//        List<purchaseOrder> list = purchaseOrderRepository.findBySupplierAndInvDate(po.getSupplier(), po.getInvDate());
//        //sub order logic
//        Integer subOrder = purchaseOrderRepository.countBySupplierAndInvDate(po.getSupplier(), po.getInvDate());
//        Integer orderNo1 = subOrder + 1;
//        String invNo = "";
//        System.out.println("dayWise sub order--- " + orderNo1);
//        if (list.size() > 0) {
//            String invNumber = list.get(0).getInvNo();  // DB Invoice Number --- FD-2023-04-23-VDPL_007-001/1
//            String[] arrOfStr = invNumber.split("/", 2);
//            System.out.println("arrOfStr --- " + arrOfStr[0]);
//            System.out.println("arrOfStr --- " + arrOfStr[1]);
//            System.out.println("dayWise main order --- " + list.size());
//            System.out.println("DB Invoice Number --- " + invNumber);
//            invNo = arrOfStr[0] + "/" + orderNo1.toString();
//            po.setInvNo(invNo);
//        } else {
//            System.out.println("dayWise main order --- " + list.size());
//            //001
//            String format = String.format("%03d", list.size() + 1);
//
//            if (supplierName.size() > 0) {
//
//                String format1 = String.format("%03d", supplierName.size() + 1);
//                invNo = "FD-" + po.getInvDate() + "-" + po.getSupplier() + "_" + format1 + "/" + orderNo1.toString();
//            } else {
//
//                invNo = "FD-" + po.getInvDate() + "-" + po.getSupplier() + "_" + format + "/" + orderNo1.toString();
//            }
//            po.setInvNo(invNo);
//        }
//        purchaseOrderRepository.save(po);
//        Map<String, String> respBody = new HashMap<>();
//        return "redirect:/purchase";
//    }

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
                po.setVehicleNo(list.getVehicleNo());
                po.setMilkType(list.getMilkType());
                po.setQuantity(list.getQuantity());
                po.setFatP(list.getFatP());
                po.setSnfP(list.getSnfP());
                po.setTsRate(list.getTsRate());
                po.setLtrRate(list.getLtrRate());
                po.setAmt(list.getAmt());
                po.setPaymentStatus(list.getPaymentStatus());
                po.setBankName(list.getBankName());
                po.setIfsc(list.getIfsc());
                po.setPaymentDate(list.getPaymentDate());
                po.setCode(list.getCode());
                po.setInvNo(invNo+subOrder);

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
        po.setInvDate(LocalDate.parse(body.get("invDate")));
        po.setRecDate(LocalDate.parse(body.get("recDate")));

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
        po.setInvDate(LocalDate.parse(body.get("invDate")));
        po.setRecDate(LocalDate.parse(body.get("recDate")));
        // excel data list
        List<purchaseOrder> list = purchaseOrderRepository.findBySupplierAndInvDateBetween(po.getSupplier(), po.getInvDate(), po.getRecDate());
        List<supplier> Suppliers = supplierRepository.findByOrderByIdDesc();
        model.addAttribute("supplier", Suppliers);
        model.addAttribute("list", list);
        System.out.println("Excel Size -- " + list.size());
        return "/purchaseView";
    }

}
