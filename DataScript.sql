select * from realtime;
select count(*) from realtime;
select * from challanlist;
select * from trucklist;
insert into realtime (source,destination,) values ("Mumbai","delhi","xyz","t7","A0",200,"Available");
UPDATE challanList SET status = 'Started'  WHERE truckId = 't7' AND status = 'Alloted' ;
insert into realtime values ("Mumbai","delhi","xyz","t8","A1",200,"Available");
insert into trucklist values ("Zeta",'t5',240);

desc trucklist;

select * from trucklist T join realtime Rt on T.truckId = Rt.truckId;
update realtime Rt, trucklist T set Rt.remainingCapacity = T.capacity where Rt.truckId = T.truckId;

truncate realtime;

insert into realtime (source,destination,currentLocation,truckId,driverName,remainingCapacity,status) 
			values ("Mumbai","delhi","xyz","t7","A0",200,"Available");

create procedure createBlankRealTimeTable();
insert into realtime (truckId) values ("t5");

update realtime Rt, trucklist T set source = 'Mumbai', Rt.driverName = T.driverName, 
									Rt.remainingCapacity = T.capacity, status = 'Available' where Rt.truckId = T.truckId;