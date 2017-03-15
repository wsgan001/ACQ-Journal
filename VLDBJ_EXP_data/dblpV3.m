set(gcf,'color',[1, 1, 1]);
set(gcf,'unit','centimeters','position',[10 5 9 7]);

x=[1, 2, 3, 4, 5];
y1 = [1516.32034228	1518.18363289	1416.47559525	1417.8242403699999	1520.99639063];
y2 = [14129.37854077	13898.22267295	13980.10595366	13998.486173340001 	13977.0281617];
y3 = [22.95339540000003	22.476135909999996	20.890965970000003	21.51948807	22.40953162];

p1= semilogy(x, y1, '-k^');
hold on;
p2 = semilogy(x, y2, '-ko');
hold on;
p3 = semilogy(x, y3, '-kd');

xlabel('the length of query set in Q');
ylabel('time (ms)');

axis([0.5 5.5 10.0 15000]);

set(gca, 'xtick', 1:5, 'XTickLabel', {'2','3','4','5','6'});
leg=legend('basic-g-v2','basic-w-v2','MDec', 2);
set(leg,'edgecolor','white');

set(gca, 'FontSize', 12);
set(get(gca, 'XLabel'), 'FontSize',12);
set(get(gca, 'YLabel'), 'FontSize',12);
set(findall(gcf,'type','line'),'linewidth',1.5);
set(gca, 'LineWidth', 1.5);