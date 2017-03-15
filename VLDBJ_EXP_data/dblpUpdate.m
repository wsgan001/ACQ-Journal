set(gcf,'color',[1, 1, 1]);
set(gcf,'unit','centimeters','position',[10 5 9 7]);

x=[1, 2, 3, 4, 5];
y1 = [3337.83426112	3315.2687069800003	3311.10081287	3301.2477142299996	3303.87523058];
y2 = [0.36165856	12.97244769	9.81531188	4.87220158	0.12547063];
y3 = [221.46351326	66.4599927	25.428799530000004	9.4807295	2.61061321];

p1= semilogy(x, y1, '-k^');
hold on;
p2 = semilogy(x, y2, '-ko');
hold on;
p3 = semilogy(x, y3, '-kd');

xlabel('the core number of the vertex');
ylabel('time (ms)');

axis([0.5 5.5 0.1 10000]);

set(gca, 'xtick', 1:5, 'XTickLabel', {'5','10','15','20','25'});
leg=legend('rebuild','insert','delete', 2);
set(leg,'edgecolor','white');

set(gca, 'FontSize', 12);
set(get(gca, 'XLabel'), 'FontSize',12);
set(get(gca, 'YLabel'), 'FontSize',12);
set(findall(gcf,'type','line'),'linewidth',1.5);
set(gca, 'LineWidth', 1.5);