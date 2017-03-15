set(gcf,'color',[1, 1, 1]);
set(gcf,'unit','centimeters','position',[10 5 9 7]);

x=[1, 2, 3, 4, 5, 6, 7, 8, 9, 10];
y1 = [6105.62	1596.43	556.99	264.17	128.51	75.24	44.8	30.41	20.39	13.85];
y2 = [69943.37	14343.11	4138.66	1567.26	716	383.92	221.72	136.35	87.04	55.86];
y3 = [170356.86	32530.1	8654.36	3049.98	1325.12	681.08	380.8	230.03	144.83	92.01];
y4 = [256731.51	47584.86	12362.3	4282.87	1850.52	943.37	526.32	317.05	199.49	126.25];
y5 = [335297.77	61455.3	15852.37	5465.44	2355.88	1198.82	668.73	403.41	254.56	160.97];

p1= plot(x, y1, '-kd');
hold on;
p2 = plot(x, y2, '-ks');
hold on;
p3 = plot(x, y3, '-k*');
hold on;
p4 = plot(x, y4, '-kv');
hold on;
p5 = plot(x, y5, '-k^');

xlabel('the number of shared keywords');
ylabel('the number of vertices');

axis([0.5 10.5 0.0 400000]);

set(gca, 'xtick', 1:10, 'XTickLabel', {'1','2','3','4','5','6','7','8','9','10'});
leg=legend('20%','40%','60%','80%','100%', 1);
set(leg,'edgecolor','white');

set(gca, 'FontSize', 12);
set(get(gca, 'XLabel'), 'FontSize',12);
set(get(gca, 'YLabel'), 'FontSize',12);
set(findall(gcf,'type','line'),'linewidth',1.5);
set(gca, 'LineWidth', 1.5);