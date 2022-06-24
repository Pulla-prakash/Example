package com.vcare.controller;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.vcare.beans.Appointment;
import com.vcare.beans.Department;
import com.vcare.beans.Doctor;
import com.vcare.beans.DoctorAvailability;
import com.vcare.beans.Employees;
import com.vcare.beans.HospitalBranch;
import com.vcare.beans.MyQr;
import com.vcare.beans.Patients;
import com.vcare.beans.Services;
import com.vcare.repository.AppointmentRepository;
import com.vcare.repository.DoctorAvailabilityRepository;
import com.vcare.repository.DoctorRepository;
import com.vcare.repository.ServiceRepository;
import com.vcare.service.DepartmentService;
import com.vcare.service.DoctorService;
import com.vcare.service.EmployeesService;
import com.vcare.service.HospitalBranchService;
import com.vcare.service.PatientsService;
import com.vcare.utils.VcareUtilies;

@Controller
@RequestMapping("")
public class EmployeesController {

	@Autowired
	EmployeesService employeeService;
	@Autowired
	DepartmentService departmentService;
	@Autowired
	ServiceRepository serviceRepository;

	@Autowired

	HospitalBranchService hospitalBranchService;

	static Logger log = Logger.getLogger(EmployeesController.class.getClass());

	@GetMapping("/List")
	public String getAllEmployees(Model model) {

		List<Employees> empList = employeeService.getAllEmployees();
		log.info("List:::::" + empList);

		List<Employees> list = new ArrayList<>();
		for (Employees emp : empList) {
			if (emp.getIsactive() == 'Y' || emp.getIsactive() == 'y') {
				list.add(emp);
			}
		}
		model.addAttribute("employeesList", list);

		return "employedetails";
	}

	@RequestMapping(value = "/empForm/{id}", method = RequestMethod.GET)
	public String showForm(Model model, @PathVariable("id") int id,
			@ModelAttribute(value = "employeeObj") Employees objEmployee) {
		Department objDepart = departmentService.getDepartmentById(id);
		model.addAttribute("department", objDepart);

		log.info("name:" + objDepart.getDepartmentId());
		model.addAttribute("obj", objDepart.getDepartmentName());
		log.info("NAME:" + objDepart.getDepartmentName());
		Map<Integer, String> objDep = dropdownDepat();
		model.addAttribute("Object", objDep);
		model.addAttribute("Id", objDep.keySet());
		model.addAttribute("employeeObj", objEmployee);
		return "employeeform";
	}

	@RequestMapping(value = "/empForm/{hbid}/{id}", method = RequestMethod.GET)
	public String showForm(Model model, @PathVariable("hbid") int hbid, @PathVariable("id") int id,
			@ModelAttribute(value = "employeeObj") Employees objEmployee) {
		HospitalBranch hbObj = hospitalBranchService.getHospitalbranchId(hbid);
		model.addAttribute("hObj", hbObj);
		Department objDepart = departmentService.getDepartmentById(id);
		model.addAttribute("department", objDepart);
		log.info("name:" + objDepart.getDepartmentId());
		model.addAttribute("obj", objDepart.getDepartmentName());
		log.info("NAME:" + objDepart.getDepartmentName());
		Map<Integer, String> objDep = dropdownDepat();
		model.addAttribute("Object", objDep);
		model.addAttribute("Id", objDep.keySet());
		model.addAttribute("employeeObj", objEmployee);
		return "employeeform";
	}

	@GetMapping("/empList")
	public String employeeList() {

		return "employedetails";
	}

	@RequestMapping(value = "/saveEmployee", method = RequestMethod.POST)
	public String addEmployee(Model model, @RequestParam("files") MultipartFile file,
			@ModelAttribute(value = "employeeObj") Employees objEmployee,
			@ModelAttribute(value = "hospitalBranch") HospitalBranch hospitalBranch )
			throws IOException, WriterException {
		objEmployee.setIsactive('y');

		log.info("inside deleteEmployee id:::" + objEmployee.getEmployeeId());
		log.info("inside deleteEmployee id:::" + objEmployee.getEmployeeName());
		log.info("inside deleteEmployee id:::" + objEmployee.getEmployeePosition());

		objEmployee.setHospitalBranch(hospitalBranch);
		model.addAttribute("employeeObj", objEmployee);

		List<Employees> empList = employeeService.getAllEmployees();
		model.addAttribute("employeesList", empList);

//		Employees EmployeeObj = employeeService.validateduplicate(objEmployee.getEmail());
//		if (EmployeeObj == null) {

			log.info(objEmployee.getEmployeeId());
			String strEncPassword = VcareUtilies.getEncryptSecurePassword(objEmployee.getPassword(), "vcare");
			objEmployee.setPassword(strEncPassword);
			String data = objEmployee.toString();
			String path = "C:\\Users\\Abhi\\eclipse-workspace\\JavaTraining\\vcare-project\\src\\main\\resources\\static\\images\\qrcode_"
					+ objEmployee.getEmployeeId() + ".png";

			String charset = "UTF-8";
			Map<EncodeHintType, ErrorCorrectionLevel> hashMap = new HashMap<EncodeHintType, ErrorCorrectionLevel>();
			hashMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
			byte[] d = MyQr.createQR(data, path, charset, hashMap, 200, 200);
			objEmployee.setEmpQrCode((Base64.getEncoder().encodeToString(d)));

			// System.out.println("branchId:::::::::::"+objEmployee.getHospitalBranch().getHospitalBranchId());
			model.addAttribute("employeeObj", objEmployee);
			String str=objEmployee.getProfile();
			System.out.println(str);
			if(file.getOriginalFilename()=="") {
				System.out.println("awzsexdcfgvbhnjm"+objEmployee.getProfile());
				
				objEmployee.setProfile(str);
			}else {
			
		
			objEmployee.setProfile(Base64.getEncoder().encodeToString(file.getBytes()));
			}
			employeeService.saveEmployees(objEmployee);

			// employeeService.addEmployees(objEmployee);

		
			
		return "redirect:/List";
		
	}
//	if (hospitalBranch.getHospitalBranchId() == 0) {
//
//		int Random = (int) (Math.random() * 90);
//		hospitalBranch.setHospitalBranchId(Random);
//		hospitalBranch.setIsactive('Y');
//		hospitalBranch.setCreated(LocalDate.now());
//		
//		hospitalBranch.setHospitalBranchImage(Base64.getEncoder().encodeToString(file.getBytes()));
//
//	} else {
//		hospitalBranchService.updateHospitalbranch(hospitalBranch);
//	}
//	hospitalBranchService.addHospitalbranch(hospitalBranch);
//
//	model.addAttribute("hospitalBranchList", hospitalBranch);
//	return "redirect:/hospitalBranch/hospitalBranchList";
//}


	@GetMapping("/editEmployee/{id}")
	public String getById(Model model, @PathVariable("id") int employeeId) {

		Employees objSecEmployee = employeeService.getById(employeeId);
		Map<Integer, String> objDep = dropdownDepat();
		model.addAttribute("Object", objDep);
		model.addAttribute("department", objDep.keySet());
		log.info("inside getHospitalbranchId id is:::" + employeeService.getById(employeeId));
		model.addAttribute("employeeObj", objSecEmployee);

		return "employeeform";

	}

	@GetMapping("/deleteEmployee/{id}")
	public String deleteService(Model model, @PathVariable int id) {
		log.info("inside deleteHospitalBranch id:::" + id);
		Employees inactive = employeeService.getById(id);
		inactive.setIsactive('N');
		employeeService.UpdateEmployees(inactive);
		List<Employees> employeeList = employeeService.getAllEmployees();
		model.addAttribute("employeesList", employeeList);

		return "redirect:/List";

	}

	public Map<Integer, String> dropdownDepat() {

		Map<Integer, String> objDep = new HashMap<Integer, String>();

		List<Department> deptList = departmentService.getAllDepartments();

		for (Department depObj : deptList) {
			objDep.put(depObj.getDepartmentId(), depObj.getDepartmentName());

		}

		return objDep;
	}

	
	@Autowired
	DoctorRepository doctorRepository;
	
	@Autowired
	DoctorAvailabilityRepository availablilityRepository; 
	
	@Autowired
	AppointmentRepository appointmentRepository;
	
	
	@SuppressWarnings("unlikely-arg-type")
	@PostMapping("/selectDoc")
	public String serviceDoctor(Model model,
			@RequestParam(name = "serviceList", required = false) List<String> serviceList, Employees objEmployee,
			Appointment appointmentObj, HospitalBranch hospitalBranch, Services service,
			@RequestParam(name = "serviceName", required = false) String servName,
			@RequestParam(name = "docName", required = false) String docName, DoctorsController docController,
			final HttpServletRequest request, Date timeStart, Date timeEnd) throws Exception {
		System.out.println("hello eddi" + objEmployee.getHospitalBranch().getHospitalBranchId());
		model.addAttribute("hosp", objEmployee.getHospitalBranch().getHospitalBranchId());
		List<String> serviceNames = serviceRepository
				.BranchServices(objEmployee.getHospitalBranch().getHospitalBranchId());
		model.addAttribute("serviceList", serviceNames);
		String getQry = request.getParameter("getQry");
		String serviceName = request.getParameter("serviceName");
		if (getQry != null && getQry.equals("selectService") && serviceName != null && serviceName != "") {
			List<Doctor> doctorList = doctorRepository
					.serviceDoctors(objEmployee.getHospitalBranch().getHospitalBranchId(), serviceName);
			model.addAttribute("doctorList", doctorList);// method in repository
			System.out.println(getQry + "++++++++" + serviceName);
			System.out.println(doctorList + "++++++++" + doctorList);
			model.addAttribute("servName", serviceName);
			model.addAttribute("doctorName", docName);
			model.addAttribute("appointmentObj", appointmentObj);
			return "receptionscreen";
		}
		List<Doctor> doctorList = doctorRepository.serviceDoctors(hospitalBranch.getHospitalBranchId(),
				service.getServiceName());
		model.addAttribute("doctorList", doctorList);// method in repository String getQryDoc =
														// request.getParameter("getQryDoc");
		String doctorId = request.getParameter("doctorId");
		//System.out.println("doctorId" + getQryDoc + " doctorId" + doctorId);
		List<LocalDate> datesInRange = new ArrayList<>();
		List<String> slots = new ArrayList<>();
		List<String> days = new ArrayList<>();
		List<String> monthdate = new ArrayList<>();
		ArrayList<String> timeList = new ArrayList<>();
		
		String getQryDoc = request.getParameter("getQryDoc");
		if (getQryDoc != null && getQryDoc.equals("doctorAvailable") && doctorId != null && doctorId != "") {
			List<DoctorAvailability> docAvailable = availablilityRepository.availableDoctor(Integer.valueOf(doctorId));
			model.addAttribute("docid", docAvailable.get(0).getDoctor().getDoctorId());
			model.addAttribute("hbid", docAvailable.get(0).getDoctor().getHospitalBranchId());
			
			System.out.println("StartTimings==========" + docAvailable.get(0).getStartTimings());
			System.out.println("EndTimings==========" + docAvailable.get(0).getEndTimings());//
			DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm");
			DateFormat formatterDay = new SimpleDateFormat("EEE");
			timeStart = (Date) formatter.parse(docAvailable.get(0).getStartTimings());
			timeEnd = (Date) formatter.parse(docAvailable.get(0).getEndTimings());
			System.out.println("end" + timeEnd.getHours());
			System.out.println("start" + timeStart.getHours());
			LocalDate sd = LocalDate.of(timeStart.getYear() + 1900, timeStart.getMonth() + 1, timeStart.getDate()); // LocalDate
			// sd=LocalDate.of(2022,05,20);
			LocalDate ed = LocalDate.of(timeEnd.getYear() + 1900, timeEnd.getMonth() + 1, timeEnd.getDate()); // LocalDate
			// ed=LocalDate.of(2022,05,31);
			while (sd.isBefore(ed)) {
				datesInRange.add(sd);
				sd = sd.plusDays(1);
				String day = sd.format(DateTimeFormatter.ofPattern("EEE"));
				String month = sd.format(DateTimeFormatter.ofPattern("MMM"));
				System.out.println(day);
				System.out.println(sd);
				System.out.println(month);
				String s = Integer.toString(sd.getDayOfMonth());
				if (sd.isAfter(LocalDate.now()) || sd.isEqual(LocalDate.now())) {
					days.add(day);
					slots.add(month);
					slots.add(s);
					String slot = month.toString() + s;
					monthdate.add(sd.toString());
					model.addAttribute("date", monthdate);
				}
			}
			model.addAttribute("times", timeList);
			// model.addAttribute("monthdate", monthdate);
			model.addAttribute("days", days);
			// model.addAttribute("availList", availList);
			model.addAttribute("servName", serviceName);
			model.addAttribute("doctorName", docName);
			// System.out.println("availablilityRepository"+docAvailable.getDoctor().getDoctorId());
			return "receptionscreen";
		}
		String getQryDate = request.getParameter("getQryDate");
		LocalDate selectedDate = LocalDate.parse(request.getParameter("Date"));
		LocalDate today = LocalDate.parse(request.getParameter("Date"));
		// Getting DoctorAvailability records by doctorID
		List<DoctorAvailability> docAvailable = availablilityRepository.availableDoctor(Integer.parseInt(doctorId));
		System.out.println("StartTimings==========" + docAvailable.get(0).getStartTimings());
		// Formatting the date from html format to java
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm");
		DateFormat formatterDay = new SimpleDateFormat("EEE");// gets week days as MON,TUE....
		// getting start and end timings from DoctorAvailability and setting to Date
		timeStart = (Date) formatter.parse(docAvailable.get(0).getStartTimings());
		timeEnd = (Date) formatter.parse(docAvailable.get(0).getEndTimings());
		System.out.println("timeEndtimeEnd" + timeEnd);
		// stores only hour of endtime
		int endHour = timeEnd.getHours();
		// Setting the time to Hours:Minutes
		DateFormat df = new SimpleDateFormat("HH:mm");
		// Separating Start and End Dates to year,month,date
		LocalDate sd = LocalDate.of(timeStart.getYear() + 1900, timeStart.getMonth() + 1, timeStart.getDate());
		LocalDate ed = LocalDate.of(timeEnd.getYear() + 1900, timeEnd.getMonth() + 1, timeEnd.getDate());
		System.out.println("sdf" + formatterDay.format(timeStart));
		List<String> times = new ArrayList<>();// To store slots
		Calendar calendar = Calendar.getInstance();// get present calendar
		calendar.setTimeZone(TimeZone.getTimeZone("Asia/Calcutta"));
		int minutes = calendar.get(Calendar.MINUTE);
		
		String hospitalBranchId=request.getParameter("hospitalBranchId");
		if (getQryDate != null && getQryDate.equals("dateAvailable") && selectedDate != null) {
//			if (selectedDate.equals(null)) {
//				model.addAttribute("selectedDate", "");// Before selecting date
//			} else {
				model.addAttribute("selectedDate", selectedDate);// After Selecting date
//			}
			
			model.addAttribute("hbid", Integer.parseInt(hospitalBranchId));
			
			model.addAttribute("docid", docAvailable.get(0).getDoctor().getDoctorId());
			
			List<Doctor> docList = doctorRepository
					.serviceDoctors(objEmployee.getHospitalBranch().getHospitalBranchId(), serviceName);
			model.addAttribute("doctorList", docList);// method in repository
			System.out.println(getQry + "++++++++" + serviceName);
			System.out.println(docList + "++++++++" + docList);
			System.out.println("in if timeEnd" + timeEnd);
			model.addAttribute("servName", serviceName);
			model.addAttribute("doctorName", docName);
			model.addAttribute("appointmentObj", appointmentObj);
			// sets start-time hour and minutes and seconds
			Calendar cal = Calendar.getInstance();
			cal.set(Calendar.HOUR_OF_DAY, timeStart.getHours());
			cal.set(Calendar.MINUTE, timeStart.getMinutes());
			cal.set(Calendar.SECOND, 0);
			// Present calender for getting todays data
			Calendar calObj = Calendar.getInstance();
			// Gets listOfAppointmnetByDoctorId&Date
			List<Appointment> app = appointmentRepository.appointmentListByDoctorid(Integer.parseInt(doctorId),
					request.getParameter("Date"));
			// For storing the booked slots as string
			ArrayList<String> totalSlots = new ArrayList<>();
			for (Appointment a : app) {
				totalSlots.add(a.getSlot());
			}
			// For Present Day Slots
			System.out.println(selectedDate+"==========="+LocalDate.now());
			while (LocalDate.now().equals(selectedDate)) {
				System.out.println("=======wrgywrhrwyrwgr=========="+df.format(cal.getTime()));
				if (cal.getTime().getHours() == endHour) {
					System.out.println("endhour=="+cal.getTime().getHours());
					break;
				} else {
					// if no Appointments are booked for present day
					if (app.size() == 0) {
						// Displaying slots for only current hour
						System.out.println("No appointments are available");
						if (cal.getTime().getHours() == calObj.get(Calendar.HOUR_OF_DAY)) {
							System.out.println("Displaying slots for only current hour="+cal.getTime().getHours());
							if (cal.getTime().getMinutes() >= calObj.get(Calendar.MINUTE)) {
								timeList.add(df.format(cal.getTime()));
								model.addAttribute("times", timeList);
							}
						}
					}
					// if Appointments are booked for present day
					else {
						// For Not Displaying the slots booked
						if (totalSlots.contains(df.format(cal.getTime()).toString())) {
							System.out.println("app.contains(df.format(cal.getTime()).toString()=="
									+ totalSlots.contains(df.format(cal.getTime()).toString()));
							System.out.println(
									"df.format(cal.getTime()).toString()==" + df.format(cal.getTime()).toString());
							cal.add(Calendar.MINUTE, 10);
							continue;
						}
						// For Displaying the slots not booked
						else {
							if (cal.getTime().getHours() == calObj.get(Calendar.HOUR_OF_DAY)) {
								if (cal.getTime().getMinutes() >= calObj.get(Calendar.MINUTE)) {
									System.out.println();
									timeList.add(df.format(cal.getTime()));
									model.addAttribute("times", timeList);
								}
							}
						}
					}
					System.out.println("cal.add(Calendar.MINUTE, 10)==" + df.format(cal.getTime()));
					// Displaying slots for after current hour
					if (cal.getTime().getHours() > calObj.get(Calendar.HOUR_OF_DAY)) {
						if (cal.getTime().getMinutes() >= calObj.get(Calendar.MINUTE)) {
							System.out.println("cal.getTime()==" + cal.getTime());
						timeList.add(df.format(cal.getTime()));
						model.addAttribute("times", timeList);
						}
					}
					cal.add(Calendar.MINUTE, 10);
				}
			} // For Slots from next-day
			while (!LocalDate.now().equals(selectedDate)) {
				if (cal.getTime().getHours() == endHour) {
					today = today.minusDays(1);
					System.out.println("Nottoday" + today);
					break;
				} else {
					if (app.size() == 0) {
						timeList.add(df.format(cal.getTime()));
						cal.add(Calendar.MINUTE, 10);
						model.addAttribute("times", timeList);
					} else {
						if (totalSlots.contains(df.format(cal.getTime()).toString())) {
							cal.add(Calendar.MINUTE, 10);
							continue;
						} else {
							System.out.println("cal.getTime()==" + cal.getTime());
							timeList.add(df.format(cal.getTime()));
							cal.add(Calendar.MINUTE, 10);
							model.addAttribute("times", timeList);
						}
					}
				}
			}
			// Dates drop-down according to DoctorAvailability
			while (sd.isBefore(ed) || sd.equals(ed)) {
				datesInRange.add(sd);
				String day = sd.format(DateTimeFormatter.ofPattern("EEE"));
				String month = sd.format(DateTimeFormatter.ofPattern("MMM"));
				String s = Integer.toString(sd.getDayOfMonth());
				if (sd.isAfter(LocalDate.now()) || sd.isEqual(LocalDate.now())) {
					days.add(day);
					slots.add(month);
					slots.add(s);
					String slot = month.toString() + s;
					monthdate.add(sd.toString());
					sd = sd.plusDays(1);
					System.out.println(sd);
					model.addAttribute("date", monthdate);
				} else {
					sd = sd.plusDays(1);
				}
			}
			model.addAttribute("times", timeList);
			/* model.addAttribute("map", map); */
			model.addAttribute("monthdate", monthdate);
			model.addAttribute("days", days);
			return "receptionscreen";
		}
		model.addAttribute("appointmentObj", appointmentObj);
		return "receptionscreen";
	}

	@GetMapping("/elogin")
	public String Validation(Model model, @ModelAttribute(value = "employeeObj") Employees objEmployee,
			Appointment appointmentObj, @RequestParam(name = "serviceList", required = false) List<String> serviceList,
			HospitalBranch hospitalBranch, HttpServletRequest request) {

		if (!objEmployee.getCaptchaDepart().equals(objEmployee.getUserCaptcha())) {
			model.addAttribute("message", "Please enter valid Captcha");
			return "index";
		}
		log.info("Get doctor name : " + objEmployee.getEmployeeName());
		log.info("get doctor password  : " + objEmployee.getEmployeePosition());
		String str = VcareUtilies.getEncryptSecurePassword(objEmployee.getPassword(), "vcare");

		Employees signinObj = employeeService.getEmployee(objEmployee.getEmail(), str);
		log.info("validation details : " + signinObj);
		model.addAttribute("employeeObj", signinObj);

		if (signinObj != null) {
			log.info("===========login successfully======");
			HttpSession session = request.getSession();
			session.setAttribute("name", signinObj.getEmployeeName());
			System.out.println("hey hey" + signinObj.getHospitalBranch().getHospitalBranchId());
			model.addAttribute("hosp", signinObj.getHospitalBranch().getHospitalBranchId());
			List<String> serviceNames = serviceRepository
					.BranchServices(signinObj.getHospitalBranch().getHospitalBranchId());
			model.addAttribute("serviceList", serviceNames);
			model.addAttribute("appointmentObj", appointmentObj);

			return "receptionscreen";
		} else {
			log.info("=======invalid credentials===");
			return "index";
		}

	}

	@GetMapping("/empid/{id}")
	public String showId(Model model, @PathVariable("id") int employeeId) {
		Employees objEmployee = employeeService.getById(employeeId);
		model.addAttribute("objEmployee", objEmployee);
		return "empidcard";
	}
	
	@Autowired
	PatientsService patientService;
	@Autowired
	DoctorService doctorService;
	
	
	@RequestMapping("/offlineappform/{hbid}/{did}")
	public String offlineAppointments(Model model,@RequestParam("slot") String slot,@RequestParam("date") String date, @PathVariable("hbid") int branchId,
	@PathVariable("did") int did,Patients patientObj, @ModelAttribute(value = "employeeObj") Employees objEmployee,
	Appointment appointment) {
	HospitalBranch branch= hospitalBranchService.getHospitalbranchId(branchId);
	patientObj.setIsactive('Y');
	patientObj.setCreated(LocalDate.now());

	Doctor doc=doctorService.GetDocotorById(did);
	System.out.println("branchId"+ branch.getHospitalBranchId());
	model.addAttribute("branchId", branch.getHospitalBranchId());
	model.addAttribute("patientObj",patientObj);
	model.addAttribute("doctorId",did);
	model.addAttribute("doctor",doc);
	model.addAttribute("slot",slot);
	model.addAttribute("date",date);

	return "offlinepatientform";
	}

}