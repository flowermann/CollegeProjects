# School of Electrical Engineering
## Department of Computer Engineering and Informatics
### Computer Networks 1 (13E112PM1 / 13C112PM1)
### Project Assignment: IP Addressing, DHCP, OSPF, RIP, ACL, TELNET, DNS

Figure 1 shows the network topology that needs to be created using Cisco Packet Tracer and subsequently configured according to the instructions provided in this document.

<img width="766" height="531" alt="image" src="https://github.com/user-attachments/assets/b2bcf10c-c641-477d-a189-0bbecb896255" />


**Figure 1. Network Topology Blueprint**

The provided network is divided into a RIP segment and an OSPF segment. The OSPF segment is further split into three areas: **Area 0 (Backbone)**, **Area 1 (Totally Stubby)**, and **Area 2 (Stub)**. 

* Router **R3** acts as the Area Border Router (ABR) between Area 0 and Area 1.
* Router **R4** acts as the ABR between Area 0 and Area 2.
* Router **R2** acts as the Autonomous System Boundary Router (ASBR) between the RIP and OSPF segments.

The IP addresses for certain devices are predefined, while the network parameters for the remaining devices must be determined based on the requirements described below. The network features four designated servers whose roles are outlined in the following sections.

---

### Task 1: Basic Configuration [2 Points]
Recreate the topology shown in Figure 1 and connect all devices. Configure the IP parameters for all devices according to the following rules:
* **Router Interfaces:** Assign the lowest available IP address from the subnet they belong to.
* **Point-to-Point (P2P) Segments:** For connections between two routers, use the lowest 2 available IP addresses. The router with the lower index receives the lower IP address.
* **Servers:** Configure the exact IP addresses shown in the diagram. Set the closest router interface as their Default Gateway.
* **End Stations / PCs:** (Excluding PC0, PC1, PC4, PC5, and PC6, which will be configured dynamically later), assign the lowest available IP addresses within their respective subnets. Set the closest router interface as the Default Gateway, and specify `192.168.0.2` as the Primary DNS Server.

---

### Task 2: DHCP Configuration [2 Points]
1. **OSPF Area 1 DHCP:** Configure the DHCP server inside OSPF Area 1 to dynamically assign IP addresses and other necessary parameters to local host devices (**PC0**, **PC1**). The scope must meet the following criteria:
   * The pool of allocatable IP addresses must start from `192.168.1.23`.
   * The subnet mask must be deduced from the existing configuration.
   * Traffic leaving this subnet must route through **R3** (Default Gateway).
   * The Primary DNS Server for all devices is located in OSPF Area 0 (refer to Figure 1).

2. **RIP Segment DHCP:** On router **R5**, configure the DHCP service to dynamically assign IP addresses and necessary configuration parameters to local hosts within the RIP segment (**PC4**, **PC5**, **PC6**). The configuration must meet the following criteria:
   * The host subnet and the switch **Switch2** reside in the `8.0.0.0/24` network range; addresses must be distributed from this pool.
   * The subnet mask must be deduced from the existing configuration.
   * **Excluded Addresses:** Addresses within the following two ranges must **NOT** be assigned: `8.0.0.0/28` and `8.0.0.18 - 8.0.0.24`.
   * Traffic leaving this subnet must route through **R5** (Default Gateway).
   * The Primary DNS Server for these devices is located at `8.0.0.3` (refer to Figure 1).

---

### Task 3: RIP Protocol [2 Points]
Configure the **RIPv2** routing protocol on routers **R2** and **R5** to establish full connectivity within the RIP segment.

---

### Task 4: OSPF Protocol [2 Points]
Configure the **OSPF** routing protocol on routers **R1**, **R2**, **R3**, and **R4** to ensure complete IP reachability for all end stations within the OSPF segment. Pay close attention to the multi-area architecture and the specific area types (**Stub** and **Totally Stubby**).

---

### Task 5: Route Redistribution [1 Point]
On the boundary router (**R2**) separating the OSPF and RIP segments, perform bidirectional route redistribution to allow traffic to flow seamlessly between the two systems.
* **RIP to OSPF Redistribution:** Use a metric value of `25` and ensure that the metric type is configured so that the cost remains unchanged throughout the entire OSPF domain (OSPF Metric Type 5 E2).
* **OSPF to RIP Redistribution:** Use a seed metric value of `1`.
* All other optional redistribution parameters may be left at their default values.

---

### Task 6: TELNET [2 Points]
Enable remote management access on router **R1** using the **TELNET** protocol, and secure the access lines with a password. Verify the configuration by connecting a laptop to the router using a console cable connection.

---

### Task 7: DNS [2 Points]
* Configure the local DNS server inside OSPF Area 0 to forward all incoming name resolution queries for the **`.com`** top-level domain to the authoritative server at `8.0.0.3`.
* The DNS server located in the RIP segment must successfully resolve the domain names `www.google.com` and `google.com`.
* **Testing:** Verify the implementation by successfully opening the `google.com` web server homepage from a web browser on any host device within the OSPF segment.

---

### Task 8: Access Control Lists (ACL) [2 Points]
Implement an Access Control List (ACL) configuration on router **R5** to enforce the following security policies:
1. The DNS server located at `8.0.0.3` must only accept incoming **DNS queries** (UDP/TCP port 53). All other traffic to this host must be blocked.
2. The web server (`google.com`) must only accept incoming **HTTP requests** (TCP port 80) and **ICMP Echo Requests** (pings).
3. **ICMP Echo Requests** (pings) are permitted toward all destination devices inside the RIP segment, **except** for the DNS server (`8.0.0.3`).
